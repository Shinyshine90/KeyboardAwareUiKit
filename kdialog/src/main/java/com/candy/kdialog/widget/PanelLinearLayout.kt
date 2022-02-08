package com.candy.kdialog.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.*
import com.candy.kdialog.R

private const val TAG = "PanelLinearLayout"

class PanelLinearLayout @JvmOverloads
constructor(ctx: Context, attrs: AttributeSet? = null) : LinearLayout(ctx, attrs) {

    enum class PanelState {
        Hidden, SoftInputPanel, EmojiPanel
    }

    private var panelState = PanelState.Hidden

    private var keyboardExpand = false

    private var keyboardHeight = 0

    private val panelContainer by lazy {
        findViewById<View>(R.id.panelContainer)
    }

    private val etInput by lazy {
        findViewById<EditText>(R.id.etInput)
    }

    private val inputPanel by lazy {
        findViewById<View>(R.id.inputPanel)
    }

    private val emojiPanel by lazy {
        findViewById<View>(R.id.emojiPanel)
    }

    var showKeyboard: (Boolean) -> Unit = {}

    init {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, inset ->
            val imeVisible = inset.isVisible(WindowInsetsCompat.Type.ime())
            val imeInsets = inset.getInsets(WindowInsetsCompat.Type.ime())
            if (keyboardExpand != imeVisible) {
                onKeyboardExpand(imeVisible, imeInsets.bottom)
            }
            Log.e(TAG, "applyWindowInsets: $imeVisible $imeInsets")
            WindowInsetsCompat.CONSUMED
        }

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<View>(R.id.showEmoji).setOnClickListener {
            showKeyboard(false)
            switch(PanelState.EmojiPanel)
        }
    }

    fun switch(state: PanelState) {
        if (state == panelState) return
        when {
            panelState == PanelState.Hidden -> {
                panelState = state
                expandAnimator.cancel()
                expandAnimator.start()
            }
            state != PanelState.Hidden -> {
                panelState = state
                when (panelState) {
                    PanelState.SoftInputPanel -> {
                        inputPanel.visibility = View.VISIBLE
                        emojiPanel.visibility = View.GONE
                    }
                    PanelState.EmojiPanel -> {
                        inputPanel.visibility = View.GONE
                        emojiPanel.visibility = View.VISIBLE
                    }
                }
            }
            else -> {
                panelState = state
                expandAnimator.cancel()
                expandAnimator.reverse()
            }
        }
    }

    private fun onKeyboardExpand(keyboardExpand: Boolean, height: Int) {
        if (0 < height && height != keyboardHeight) {
            keyboardHeight = height
        }
        if (keyboardExpand) {
            switch(PanelState.SoftInputPanel)
        } else if (panelState == PanelState.SoftInputPanel) {
            switch(PanelState.Hidden)
        }
        this.keyboardExpand = keyboardExpand
    }

    private val expandAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 220L
        interpolator = LinearInterpolator()
        addUpdateListener {
            panelContainer.layoutParams.height = (keyboardHeight * (it.animatedValue as Float)).toInt()
            panelContainer.requestLayout()
        }
        addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                when (panelState) {
                    PanelState.SoftInputPanel -> {
                        inputPanel.visibility = View.VISIBLE
                        emojiPanel.visibility = View.GONE
                    }
                    PanelState.EmojiPanel -> {
                        inputPanel.visibility = View.GONE
                        emojiPanel.visibility = View.VISIBLE
                    }
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
            }
        })
    }

}