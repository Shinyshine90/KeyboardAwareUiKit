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
import com.candy.kdialog.R
import com.candy.kdialog.entity.KeyboardBottomUi

private const val TAG = "KeyboardAwareLayout"

open class KeyboardAwareLinearLayout constructor(context: Context, attrs: AttributeSet? = null):
    LinearLayout(context, attrs) {

    /**
     * trigger id map to target panel
     */
    enum class PanelState(var triggerId: Int) {
        Hidden(-1), SoftInputPanel(-1), FunctionPanel(-1)
    }

    private var currPanelState = PanelState.Hidden

    private lateinit var bottomUiLayout: KeyboardBottomUi

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

    var hideSoftInput: () -> Unit = {}

    fun setBottomBar(keyboardBottomUi: KeyboardBottomUi) {
        bottomUiLayout = keyboardBottomUi
        // add space
        addView(View(context), LayoutParams(LayoutParams.MATCH_PARENT, 0 ,1f))
        // add bottom bar view
        addView(keyboardBottomUi.bottomBar, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        // add bottom panels container
        addView(bottomPanelContainer, LayoutParams.MATCH_PARENT, 0)
        //
        keyboardBottomUi.bottomPanelRegistrations.forEach {
            val triggerId = it.key
            it.value.setTag(R.id.keyboardTag, triggerId)
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
        if (currPanelState == PanelState.SoftInputPanel) {
            adjustPanelHeight(true)
        }
    }

    /**
     * handle keyboard state change and dispatch to observers
     */
    private fun onKeyboardExpand(keyboardExpand: Boolean) {
        if (keyboardExpand) {
            switchState(PanelState.SoftInputPanel)
        } else if (currPanelState == PanelState.SoftInputPanel) {
            switchState(PanelState.Hidden)
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
        switchState(PanelState.FunctionPanel.apply {
            this.triggerId = view.id
        })
    }

    private fun switchState(targetState: PanelState) {
        when(targetState) {
            PanelState.Hidden -> {
                if (currPanelState == PanelState.Hidden) return
                //collapse panel layout
                currPanelState = targetState
                adjustPanelHeight(false)
            }
            PanelState.SoftInputPanel -> {
                if (currPanelState == PanelState.Hidden) {
                    adjustPanelHeight(true)
                }
                currPanelState = targetState
                bottomPanelContainer.removeAllViews()
            }
            PanelState.FunctionPanel -> {
                if (currPanelState == PanelState.Hidden) {
                    adjustPanelHeight(true)
                }
                currPanelState = targetState
                hideSoftInput()
                val triggerId = PanelState.FunctionPanel.triggerId
                val panelView = bottomUiLayout.bottomPanelRegistrations[triggerId] ?: return
                bottomPanelContainer.removeAllViews()
                bottomPanelContainer.addView(panelView, ViewGroup.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            }
        }
    }

    private fun adjustPanelHeight(expand: Boolean) {
        val transition = ChangeBounds()
        transition.duration = 200
        TransitionManager.beginDelayedTransition(this, transition)
        val targetHeight = if (expand) { keyboardHeight } else { 0 }
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