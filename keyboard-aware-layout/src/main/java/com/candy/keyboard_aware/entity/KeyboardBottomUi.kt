package com.candy.keyboard_aware.entity

import android.view.View

class KeyboardBottomUi (
    val bottomBar: View,
    val bottomPanelBackgroundColor: Int,
    val bottomPanelRegistrations: Map<Int, PanelUi>
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