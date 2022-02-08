package com.example.inputpaneldialog.status

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.core.view.children
import com.candy.kdialog.utils.BarUtils
import com.candy.kdialog.utils.ScreenUtils
import com.candy.kdialog.utils.SystemUiUtils
import com.example.inputpaneldialog.R

private const val TAG = "NormalActivityT"

class InsetControlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal)
        //SystemUiCompatUtils.statusBarColor(window, Color.TRANSPARENT)
        //SystemUiCompatUtils.decorFitSystemWindow(window, false)
        SystemUiUtils.adaptDisplayCutout(window)
        findViewById<ViewGroup>(R.id.llRoot).children.forEachIndexed { index, view ->
            view.setOnClickListener {
                when (index) {
                    0 -> SystemUiUtils.showStatusBar(window)
                    1 -> SystemUiUtils.hideStatusBarSticky(window)
                    2 -> SystemUiUtils.showNavigationBar(window)
                    3 -> SystemUiUtils.hideNavigationBarSticky(window)
                    4 -> SystemUiUtils.openSoftInput(window)
                    5 -> SystemUiUtils.hideSoftInput(window)
                    6 -> Log.e(TAG, "status bar: " +
                            "visible-${BarUtils.isStatusBarVisible(window.decorView)} " +
                            "height-${BarUtils.getStatusBarHeight(window.decorView)} " +
                            "stable-${BarUtils.getStatusBarStableHeight(window.decorView)}")
                    7 -> Log.e(TAG, "navi bar: " +
                            "visible-${BarUtils.isNaviBarVisible(window.decorView)} " +
                            "height-${BarUtils.getNaviBarHeight(window.decorView)} " +
                            "stable-${BarUtils.getNaviBarStableHeight(window.decorView)}")
                    8 -> Log.e(TAG, "ime bar: " +
                            "visible-${BarUtils.isImeVisible(window.decorView)} " +
                            "height-${BarUtils.getImeHeight(window.decorView)} " +
                            "stable-${BarUtils.getImeStableHeight(window.decorView)}")
                    9 -> Log.e(TAG, "screen info: size ${ScreenUtils.getScreenSize(this)}")
                }
            }
        }
    }
}