package com.candy.kdialog.entity

import android.util.ArrayMap
import android.view.View

class KeyboardBottomUi (
    val bottomBar: View,
    val bottomPanelRegistrations: ArrayMap<Int, View>
) {
    companion object {
        const val REFLECT_TAG = 0x1024
    }
}