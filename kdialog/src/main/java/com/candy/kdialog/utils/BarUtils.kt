package com.candy.kdialog.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 系统状态栏，导航栏工具类
 * Checked android 21、 25、
 * ISSUE:
 * 1，坚果手机 api 25，获取不到导航栏高度;
 * 2，当decorView fitSystemWindow 设置为false时，获取的systemBar高度均为stable高度；
 * 3，横屏时，不同rom对NavigationBar摆放位置不同，工具类中始终获取底部NavigationBar高度；
 */
object BarUtils {

    /**
     * 状态栏是否可见
     */
    @JvmStatic fun isStatusBarVisible(view: View) =
        ViewCompat.getRootWindowInsets(view)
            ?.isVisible(WindowInsetsCompat.Type.statusBars()) ?: false

    /**
     * 获取状态栏高度
     */
    @JvmStatic fun getStatusBarHeight(view: View) =
        ViewCompat.getRootWindowInsets(view)
            ?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0

    /**
     * 获取状态栏高度，忽略可见性
     */
    @JvmStatic fun getStatusBarStableHeight(view: View) =
        ViewCompat.getRootWindowInsets(view)
            ?.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.statusBars())?.top ?: 0

    /**
     * 导航栏是否可见
     */
    @JvmStatic fun isNaviBarVisible(view: View) =
        ViewCompat.getRootWindowInsets(view)
            ?.isVisible(WindowInsetsCompat.Type.navigationBars()) ?: false

    /**
     * 获取导航栏高度
     */
    @JvmStatic fun getNaviBarHeight(view: View) =
        ViewCompat.getRootWindowInsets(view)
            ?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0

    /**
     * 获取导航栏高度，忽略可见性
     */
    @JvmStatic fun getNaviBarStableHeight(view: View) =
        ViewCompat.getRootWindowInsets(view)
            ?.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0

    /**
     * 导航栏是否可见
     */
    @JvmStatic fun isImeVisible(view: View) =
        ViewCompat.getRootWindowInsets(view)
            ?.isVisible(WindowInsetsCompat.Type.ime()) ?: false

    /**
     * 获取软键盘高度
     */
    @JvmStatic fun getImeHeight(view: View) =
        ViewCompat.getRootWindowInsets(view)
            ?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0

    /**
     * 获取软键盘高度，忽略可见性
     */
    @JvmStatic fun getImeStableHeight(view: View) =
        ViewCompat.getRootWindowInsets(view)
            ?.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.ime())?.bottom ?: 0

    
}