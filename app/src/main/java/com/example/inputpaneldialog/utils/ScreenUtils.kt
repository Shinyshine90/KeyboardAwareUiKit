package com.example.inputpaneldialog.utils

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager

/**
 * 屏幕尺寸工具类
 */
object ScreenUtils {

    fun getScreenWidth(context: Context) = getScreenSize(context).second

    fun getScreenHeight(context: Context) = getScreenSize(context).second

    fun getScreenSize(context: Context): Pair<Int, Int> {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return 0 to 0
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

}







