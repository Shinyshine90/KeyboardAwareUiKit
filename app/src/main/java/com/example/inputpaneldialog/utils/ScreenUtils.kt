package com.example.inputpaneldialog.utils

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager

fun Context.getScreenWidth() = getScreenSize().second

fun Context.getScreenHeight() = getScreenSize().second

fun Context.getScreenSize(): Pair<Int, Int> {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        windowManager.currentWindowMetrics.bounds.run {
            width() to height()
        }
    } else {
        val size = Point()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getRealSize(size)
        size.x to size.y
    }
}






