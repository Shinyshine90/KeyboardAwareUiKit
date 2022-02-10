package com.candy.kdialog.entity

import android.view.View
import android.util.ArrayMap

class KeyboardBottomUi (
    val bottomBar: View,
    val bottomPanelRegistrations: ArrayMap<Int, PanelUi>
)

class PanelUi(
    val panelView: View,
    val layoutHeight: LayoutHeight
) {

    enum class LayoutMode {
        EXACTLY, ADJUST_KEYBOARD
    }

    data class LayoutHeight(val mode: LayoutMode, val desireHeight: Int = 0)
}

fun createExactlyHeightPanel(panelView: View, desireHeight: Int) =
    PanelUi(panelView, PanelUi.LayoutHeight(PanelUi.LayoutMode.EXACTLY, desireHeight))

fun createAdjustKeyboardPanel(panelView: View) =
    PanelUi(panelView, PanelUi.LayoutHeight(PanelUi.LayoutMode.ADJUST_KEYBOARD))