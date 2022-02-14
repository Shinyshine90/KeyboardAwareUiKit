package com.candy.keyboard_aware.inter

import android.view.View
import com.candy.keyboard_aware.entity.KeyboardBottomUi

internal interface IKeyboardAwareLayout {

    fun createContentUi(): View

    fun createKeyboardBottomUi(): KeyboardBottomUi
}