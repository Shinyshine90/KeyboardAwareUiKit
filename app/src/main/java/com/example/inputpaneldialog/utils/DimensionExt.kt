package com.example.inputpaneldialog.utils

import android.content.Context

fun Context.dp2px(value: Float): Int {
    return (resources.displayMetrics.density * value + 0.5f).toInt()
}