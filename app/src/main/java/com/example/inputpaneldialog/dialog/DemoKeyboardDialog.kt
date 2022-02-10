package com.example.inputpaneldialog.dialog

import android.app.Activity
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import com.candy.kdialog.KeyboardDialog
import com.candy.kdialog.entity.KeyboardBottomUi
import com.example.inputpaneldialog.R

class DemoKeyboardDialog(private val activity: Activity): KeyboardDialog(activity){

    override fun createBottomBar(): KeyboardBottomUi {
        val bottomBar = inflateView(R.layout.layout_dialog_bottom_bar)
        val bottomPanelRegistrations = ArrayMap<Int, View>()
        bottomPanelRegistrations[R.id.btn_emoji] = inflateView(R.layout.layout_dialog_bottom_panel_emoji)
        bottomPanelRegistrations[R.id.btn_func] = inflateView(R.layout.layout_dialog_bottom_panel_func)
        return KeyboardBottomUi(bottomBar, bottomPanelRegistrations)
    }

    private fun inflateView(id: Int) = LayoutInflater.from(activity).inflate(id, null)
}