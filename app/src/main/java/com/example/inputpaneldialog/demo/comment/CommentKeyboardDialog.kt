package com.example.inputpaneldialog.demo.comment

import android.app.Activity
import android.content.Context
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.candy.keyboard_aware.KeyboardAwareLayout
import com.candy.keyboard_aware.KeyboardDialog
import com.candy.keyboard_aware.entity.KeyboardBottomUi
import com.candy.keyboard_aware.entity.PanelUi
import com.candy.keyboard_aware.entity.createAdjustKeyboardPanel
import com.candy.keyboard_aware.entity.createExactlyHeightPanel
import com.example.inputpaneldialog.R
import com.example.inputpaneldialog.helper.bindEmoji
import com.example.inputpaneldialog.helper.bindFunction
import com.example.inputpaneldialog.utils.dp2px

class CommentKeyboardDialog(private val activity: Activity): KeyboardDialog(activity){

    override fun createKeyboardAwareLayout(context: Context): KeyboardAwareLayout = DialogKeyboardLayout(context)

    class DialogKeyboardLayout constructor(context: Context, attrs: AttributeSet? = null):
        KeyboardAwareLayout(context, attrs) {

        override fun createContentUi() = View(context)

        override fun createKeyboardBottomUi(): KeyboardBottomUi {

            val bottomBar = inflateView(R.layout.layout_dialog_bottom_bar)

            val bottomPanelBg = ContextCompat.getColor(context, R.color.white_light)

            val emojiPanel = inflateView(R.layout.layout_panel_emoji) as RecyclerView

            val functionPanel = inflateView(R.layout.layout_panel_function)

            bindEmoji(emojiPanel)

            bindFunction(functionPanel.findViewById(R.id.rv_function))

            val bottomPanelRegistrations = ArrayMap<Int, PanelUi>()
            bottomPanelRegistrations[R.id.btn_emoji] = createExactlyHeightPanel(
                emojiPanel, context.dp2px(400f))
            bottomPanelRegistrations[R.id.btn_func] =
                createAdjustKeyboardPanel(functionPanel)

            return KeyboardBottomUi(bottomBar, bottomPanelBg, bottomPanelRegistrations)
        }

        private fun inflateView(id: Int) = LayoutInflater.from(context).inflate(id, null)
    }

    override fun show() {
        super.show()
        window?.decorView?.findViewById<EditText>(R.id.et_input)?.requestFocus()
    }
}