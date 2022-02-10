package com.example.inputpaneldialog.dialog

import android.app.Activity
import android.util.ArrayMap
import android.view.LayoutInflater
import com.candy.kdialog.KeyboardDialog
import com.candy.kdialog.entity.KeyboardBottomUi
import com.candy.kdialog.entity.PanelUi
import com.candy.kdialog.entity.createAdjustKeyboardPanel
import com.candy.kdialog.entity.createExactlyHeightPanel
import com.example.inputpaneldialog.R

class DemoKeyboardDialog(private val activity: Activity): KeyboardDialog(activity){

    override fun createBottomBar(): KeyboardBottomUi {
        val bottomBar = inflateView(R.layout.layout_dialog_bottom_bar)
        val bottomPanelRegistrations = ArrayMap<Int, PanelUi>()
        bottomPanelRegistrations[R.id.btn_emoji] = createExactlyHeightPanel(inflateView(R.layout.layout_dialog_bottom_panel_emoji), 1200)
        bottomPanelRegistrations[R.id.btn_func] = createAdjustKeyboardPanel(inflateView(R.layout.layout_dialog_bottom_panel_func))
        return KeyboardBottomUi(bottomBar, bottomPanelRegistrations)
    }

    private fun inflateView(id: Int) = LayoutInflater.from(activity).inflate(id, null)
}