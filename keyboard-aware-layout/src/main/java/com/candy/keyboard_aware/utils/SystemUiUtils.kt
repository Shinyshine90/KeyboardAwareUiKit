package com.candy.keyboard_aware.utils

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

object SystemUiUtils {

    /**
     * 适配刘海屏
     * DecorView的内容区域延伸到刘海区域, ContentView 不绘制到刘海区域
     */
    fun adaptDisplayCutout(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.apply {
                attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                attributes = attributes
            }
        }
    }

    fun decorFitSystemWindow(window: Window, fit: Boolean) {
        WindowCompat.setDecorFitsSystemWindows(window, fit)
    }

    fun statusBarColor(window: Window, color: Int) {
        window.statusBarColor = color
    }

    fun transparentStatusBar(window: Window) {
        window.statusBarColor = Color.TRANSPARENT
    }

    fun hideStatusBar(window: Window) {
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.statusBars())
    }

    fun hideStatusBarSticky(window: Window) {
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.statusBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    fun showStatusBar(window: Window) {
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    fun transparentNavigationBar(window: Window) {
        window.navigationBarColor = Color.TRANSPARENT
    }

    fun navigationBarColor(window: Window, color: Int) {
        window.navigationBarColor = color
    }

    fun hideNavigationBar(window: Window) {
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }

    fun hideNavigationBarSticky(window: Window) {
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    fun showNavigationBar(window: Window) {
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.show(WindowInsetsCompat.Type.navigationBars())
    }

    fun immersionMode(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    }

    fun openSoftInput(window: Window, view: View? = null) {
        val focus = view ?: window.decorView
        WindowInsetsControllerCompat(window, focus).show(WindowInsetsCompat.Type.ime())
    }

    fun hideSoftInput(window: Window, view: View? = null) {
        val focus = view ?: window.decorView
        WindowInsetsControllerCompat(window, focus).hide(WindowInsetsCompat.Type.ime())
    }
}