package com.example.inputpaneldialog.demo.chat

import android.content.Context
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.candy.keyboard_aware.KeyboardAwareLayout
import com.candy.keyboard_aware.entity.KeyboardBottomUi
import com.candy.keyboard_aware.entity.PanelUi
import com.candy.keyboard_aware.entity.createAdjustKeyboardPanel
import com.candy.keyboard_aware.entity.createExactlyHeightPanel
import com.example.inputpaneldialog.R

class ChatKeyboardLayout constructor(context: Context, attrs: AttributeSet? = null):
    KeyboardAwareLayout(context, attrs) {

    override fun createContentUi(): View =
        inflateView(R.layout.layout_chat_content_view)

    override fun createKeyboardBottomUi(): KeyboardBottomUi {
        val bottomBar = inflateView(R.layout.layout_dialog_bottom_bar)
        val bottomPanelRegistrations = ArrayMap<Int, PanelUi>()
        bottomPanelRegistrations[R.id.btn_emoji] = createExactlyHeightPanel(
            inflateView(R.layout.layout_dialog_bottom_panel_emoji), 1200)
        bottomPanelRegistrations[R.id.btn_func] =
            createAdjustKeyboardPanel(inflateView(R.layout.layout_dialog_bottom_panel_func))
        return KeyboardBottomUi(bottomBar, bottomPanelRegistrations)
    }

    private fun inflateView(id: Int) = LayoutInflater.from(context).inflate(id, null)

}