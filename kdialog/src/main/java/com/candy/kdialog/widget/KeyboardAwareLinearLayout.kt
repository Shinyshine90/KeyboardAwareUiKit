package com.candy.kdialog.widget

import android.content.Context
import android.graphics.Color
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.candy.kdialog.entity.KeyboardBottomUi
import com.candy.kdialog.entity.PanelUi

private const val TAG = "KeyboardAwareLayout"

open class KeyboardAwareLinearLayout constructor(context: Context, attrs: AttributeSet? = null):
    LinearLayout(context, attrs) {

    /**
     * trigger id map to target panel
     */
    /*enum class PanelState(var triggerId: Int) {
        Hidden(-1), SoftInputPanel(-1), FunctionPanel(-1)
    }*/

    private data class PanelType(
        val type: Int,
        val triggerId: Int = View.NO_ID
    )

    private fun PanelType.isHiddenType() = type == TYPE_HIDDEN

    private fun PanelType.isSoftInputType() = type == TYPE_SOFT_INPUT

    private fun PanelType.isFunctionType() = type == TYPE_FUNCTION

    companion object {

        private const val TYPE_HIDDEN = 0

        private const val TYPE_SOFT_INPUT = 1

        private const val TYPE_FUNCTION = 2

        private val PanelHiddenType = PanelType(TYPE_HIDDEN)

        private val PanelSoftInputType = PanelType(TYPE_SOFT_INPUT)
    }

    private var currPanelState = PanelHiddenType

    private lateinit var bottomUi: KeyboardBottomUi

    /**
     * keyboard expanded or not
     */
    private var keyboardExpand = false

    /**
     * keyboard height in pixel
     */
    private var keyboardHeight = 400

    /**
     * keyboard expand state observable
     */
    private val keyboardStateCallbacks = mutableListOf<(Boolean) -> Unit>()

    private val keyboardHeightChangeCallbacks = mutableListOf<(Int) -> Unit>()

    private val bottomPanelContainer = FrameLayout(context).apply {
        setBackgroundColor(Color.BLACK)
    }

    var openSoftInput: (Boolean) -> Unit = {}

    fun setBottomBar(keyboardBottomUi: KeyboardBottomUi) {
        bottomUi = keyboardBottomUi
        // add space
        addView(View(context), LayoutParams(LayoutParams.MATCH_PARENT, 0 ,1f))
        // add bottom bar view
        addView(keyboardBottomUi.bottomBar, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        // add bottom panels container
        addView(bottomPanelContainer, LayoutParams.MATCH_PARENT, 0)
        //
        keyboardBottomUi.bottomPanelRegistrations.forEach {
            val triggerId = it.key
            keyboardBottomUi.bottomBar.findViewById<View>(triggerId)
                .setOnClickListener(::onClickTrigger)
        }
    }

    /**
     * observe keyboard expand state & keyboard height, by WindowInsets Api
     */
    private fun awareKeyboard() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, inset ->
            val imeVisible = inset.isVisible(WindowInsetsCompat.Type.ime())
            val imeInsets = inset.getInsets(WindowInsetsCompat.Type.ime())
            onKeyboardHeightChanged(imeInsets.bottom)
            if (keyboardExpand != imeVisible) {
                onKeyboardExpand(imeVisible)
            }
            Log.e(TAG, "applyWindowInsets: $imeVisible $imeInsets")
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun onKeyboardHeightChanged(height: Int) {
        if (height <= 0 || height == keyboardHeight) return
        keyboardHeight = height
        //adjust for keyboard height changed
        if (currPanelState.type == TYPE_SOFT_INPUT) {
            adjustPanelHeight(currPanelState)
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
        switchState(PanelType(TYPE_FUNCTION, view.id))
    }

    private fun switchState(targetType: PanelType) {
        //当处于Function状态，点击相同trigger 收起panel
        if (currPanelState.isFunctionType() && targetType.isFunctionType()
            && currPanelState.triggerId == targetType.triggerId ) {
            openSoftInput(true)
            switchState(PanelSoftInputType)
            return
        }
        when {
            // collapse panel
            targetType.isHiddenType() -> {
                adjustPanelHeight(targetType)
                currPanelState = targetType
                bottomPanelContainer.removeAllViews()
            }
            // switch to keyboard panel
            targetType.isSoftInputType() -> {
                adjustPanelHeight(targetType)
                currPanelState = targetType
                bottomPanelContainer.removeAllViews()
            }
            // switch to function panel
            else -> {
                adjustPanelHeight(targetType)
                currPanelState = targetType
                openSoftInput(false)
                val triggerId = targetType.triggerId
                val panelUi = bottomUi.bottomPanelRegistrations[triggerId] ?: return
                bottomPanelContainer.removeAllViews()
                bottomPanelContainer.addView(panelUi.panelView, ViewGroup.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            }
        }
    }

    private fun adjustPanelHeight(targetPanelType: PanelType) {
        val transition = ChangeBounds()
        TransitionManager.beginDelayedTransition(this, transition)
        val targetHeight = when {
            targetPanelType.isHiddenType() -> 0
            targetPanelType.isSoftInputType() -> keyboardHeight
            else -> {
                when {
                    !targetPanelType.isFunctionType() -> {
                        throw RuntimeException("function panel type error")
                    }
                    targetPanelType.triggerId == View.NO_ID -> {
                        throw RuntimeException("invalidate trigger id")
                    }
                    else -> {
                        val panelUi = bottomUi.bottomPanelRegistrations[targetPanelType.triggerId]
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
    }

    init {
        orientation = VERTICAL
        awareKeyboard()
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

}