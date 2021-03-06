package com.candy.keyboard_aware

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.candy.keyboard_aware.entity.KeyboardBottomUi
import com.candy.keyboard_aware.entity.PanelUi
import com.candy.keyboard_aware.inter.IKeyboardAwareLayout
import com.candy.keyboard_aware.utils.SystemUiUtils
import com.candy.keyboard_aware.utils.ViewUtils

private const val TAG = "KeyboardAwareLayout"

/**
 * setOnApplyWindowInsetsListener 在不同安卓版本上表现不一致，低版本不回调
 */
abstract class KeyboardAwareLayout constructor(context: Context, attrs: AttributeSet? = null):
    LinearLayout(context, attrs), IKeyboardAwareLayout {

    private data class PanelInfo(
        val type: Int,
        val triggerId: Int = View.NO_ID
    )

    private fun PanelInfo.isHiddenType() = type == TYPE_HIDDEN

    private fun PanelInfo.isSoftInputType() = type == TYPE_SOFT_INPUT

    private fun PanelInfo.isFunctionType() = type == TYPE_FUNCTION

    companion object {

        private const val TYPE_HIDDEN = 0

        private const val TYPE_SOFT_INPUT = 1

        private const val TYPE_FUNCTION = 2

        private val PanelHiddenType = PanelInfo(TYPE_HIDDEN)

        private val PanelSoftInputType = PanelInfo(TYPE_SOFT_INPUT)
    }

    private var currPanelState = PanelHiddenType

    private val contentUi: View

    private val bottomUi: KeyboardBottomUi

    /**
     * keyboard expanded or not
     */
    var keyboardExpand = false
        private set

    /**
     * keyboard height in pixel
     */
    var keyboardHeight = 400
        private set

    var displayBottomUiHeight = 0
        private set

    /**
     * keyboard expand state observable
     */
    private val keyboardStateCallbacks = mutableListOf<(Boolean) -> Unit>()

    private val keyboardHeightChangeCallbacks = mutableListOf<(Int) -> Unit>()

    private val bottomUiHeightChangeCallbacks = mutableListOf<(Int) -> Unit>()

    private val bottomPanelContainer = FrameLayout(context)

    var openSoftInput: (Boolean) -> Unit = ::openSoftInputInternal

    init {
        orientation = VERTICAL
        bottomUi = createKeyboardBottomUi()
        contentUi = createContentUi()
        // add contentView
        addView(contentUi, LayoutParams(LayoutParams.MATCH_PARENT, 0 ,1f))
        // add bottom bar view
        addView(bottomUi.bottomBar, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        // add bottom panels container
        bottomPanelContainer.setBackgroundColor(bottomUi.bottomPanelBackgroundColor)
        addView(bottomPanelContainer, LayoutParams.MATCH_PARENT, 0)
        //
        bottomUi.bottomPanelRegistrations.forEach {
            val triggerId = it.key
            bottomUi.bottomBar.findViewById<View>(triggerId)
                .setOnClickListener(::onClickTrigger)
        }
        bottomUi.bottomBar.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                bottomUi.bottomBar.viewTreeObserver.removeOnGlobalLayoutListener(this)
                adjustPanelHeight(currPanelState)
            }

        })
        awareKeyboard()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        var parent: ViewParent? = this
        while (parent != null) {
            (parent as? View)?.fitsSystemWindows = false
            parent = parent.parent
        }
    }

    /**
     * observe keyboard expand state & keyboard height, by WindowInsets Api
     */
    private fun awareKeyboard() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, inset ->
            val statusBarInsets = inset.getInsets(WindowInsetsCompat.Type.statusBars())
            ViewUtils.findContentLayout(this)?.updatePadding(top = statusBarInsets.top)

            val imeVisible = inset.isVisible(WindowInsetsCompat.Type.ime())
            val imeInsets = inset.getInsets(WindowInsetsCompat.Type.ime())
            onKeyboardHeightChanged(imeInsets.bottom)
            if (keyboardExpand != imeVisible) {
                onKeyboardExpand(imeVisible)
            }
            Log.e(TAG, "applyWindowInsets: $imeVisible $imeInsets")
            inset
        }
    }

    private fun onKeyboardHeightChanged(height: Int) {
        if (height <= 0 || height == keyboardHeight) return
        keyboardHeight = height
        //adjust for keyboard height changed
        if (currPanelState.type == TYPE_SOFT_INPUT) {
            adjustPanelHeight(currPanelState)
        }
        keyboardHeightChangeCallbacks.forEach {
            it(keyboardHeight)
        }
    }

    /**
     * handle keyboard state change and dispatch to observers
     */
    private fun onKeyboardExpand(keyboardExpand: Boolean) {
        if (keyboardExpand) {
            switchState(PanelSoftInputType)
        } else if (currPanelState == PanelSoftInputType) {
            switchState(PanelHiddenType)
        }
        this.keyboardExpand = keyboardExpand
        notifyChanged()
    }

    private fun notifyChanged() {
        keyboardStateCallbacks.forEach { callback ->
            callback(keyboardExpand)
        }
    }

    private fun onClickTrigger(view: View) {
        val targetType = PanelInfo(TYPE_FUNCTION, view.id)
        //当处于Function状态，点击相同trigger 收起panel
        if (currPanelState.isFunctionType() && targetType.isFunctionType()
            && currPanelState.triggerId == targetType.triggerId ) {
            openSoftInput(true)
            switchState(PanelSoftInputType)
            return
        }
        switchState(targetType)
    }

    private fun switchState(targetInfo: PanelInfo) {
        when {
            // collapse panel
            targetInfo.isHiddenType() -> {
                adjustPanelHeight(targetInfo)
                currPanelState = targetInfo
                bottomPanelContainer.removeAllViews()
            }
            // switch to keyboard panel
            targetInfo.isSoftInputType() -> {
                adjustPanelHeight(targetInfo)
                currPanelState = targetInfo
                bottomPanelContainer.removeAllViews()
            }
            // switch to function panel
            else -> {
                adjustPanelHeight(targetInfo)
                currPanelState = targetInfo
                openSoftInput(false)
                val triggerId = targetInfo.triggerId
                val panelUi = bottomUi.bottomPanelRegistrations[triggerId] ?: return
                bottomPanelContainer.removeAllViews()
                bottomPanelContainer.addView(panelUi.panelView, ViewGroup.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            }
        }
    }

    private fun adjustPanelHeight(targetPanelInfo: PanelInfo) {
        val transition = ChangeBounds()
        transition.duration = 180
        transition.excludeChildren(contentUi, true)
        TransitionManager.beginDelayedTransition(this, transition)
        val targetHeight = when {
            targetPanelInfo.isHiddenType() -> 0
            targetPanelInfo.isSoftInputType() -> keyboardHeight
            else -> {
                when {
                    !targetPanelInfo.isFunctionType() -> {
                        throw RuntimeException("function panel type error")
                    }
                    targetPanelInfo.triggerId == View.NO_ID -> {
                        throw RuntimeException("invalidate trigger id")
                    }
                    else -> {
                        val panelUi = bottomUi.bottomPanelRegistrations[targetPanelInfo.triggerId]
                        when {
                            panelUi == null -> 0
                            panelUi.layoutHeight.mode == PanelUi.LayoutMode.ADJUST_KEYBOARD -> keyboardHeight
                            else -> panelUi.layoutHeight.desireHeight

                        }
                    }
                }
            }
        }
        bottomPanelContainer.updateLayoutParams {
            this.height = targetHeight
        }
        displayBottomUiHeight = bottomUi.bottomBar.height + targetHeight
        bottomUiHeightChangeCallbacks.forEach {
            it((displayBottomUiHeight))
        }
    }

    private fun openSoftInputInternal(expand: Boolean) {
        val expandFunc = { activity: Activity ->
            activity.window?.apply {
                if (expand) {
                    SystemUiUtils.openSoftInput(this, rootView)
                } else {
                    SystemUiUtils.hideSoftInput(this, rootView)
                }
            }
        }
        when (context) {
            is Activity -> {
                expandFunc(context as Activity)
            }
            is ContextWrapper -> {
                val activity = (context as ContextWrapper).baseContext as? Activity ?: return
                expandFunc(activity)
            }
            else -> {}
        }
    }

    fun registerKeyboardChanged(callback: (Boolean) -> Unit) {
        keyboardStateCallbacks.add(callback)
    }

    fun unregisterKeyboardChanged(callback: (Boolean) -> Unit) {
        keyboardStateCallbacks.remove(callback)
    }

    fun registerKeyboardHeightChanged(callback: (Int) -> Unit) {
        keyboardHeightChangeCallbacks.add(callback)
    }

    fun unregisterKeyboardHeightChanged(callback: (Int) -> Unit) {
        keyboardHeightChangeCallbacks.remove(callback)
    }

    fun registerBottomUiHeightChanged(callback: (Int) -> Unit) {
        bottomUiHeightChangeCallbacks.add(callback)
    }

    fun unregisterBottomUiHeightChanged(callback: (Int) -> Unit) {
        bottomUiHeightChangeCallbacks.remove(callback)
    }

}