package com.candy.keyboard_aware.utils

import android.view.View
import android.view.ViewGroup

object ViewUtils {

    fun findContentLayout(view: View): View? {
        var parent = view.parent
        while (parent is ViewGroup) {
            if (parent.id == android.R.id.content) {
                return parent
            }
            parent = parent.parent
        }
        return null
    }
}