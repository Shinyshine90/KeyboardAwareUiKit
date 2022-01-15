package com.example.inputpaneldialog

import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowCompat
import com.blankj.utilcode.util.BarUtils
import com.example.inputpaneldialog.dialog.InputDialog
import com.example.inputpaneldialog.utils.SystemUiCompatUtils
import com.example.inputpaneldialog.utils.getScreenSize
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar

private const val TAG = "MainActivityTag"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSystemUi()
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btn_dialog).setOnClickListener {
            InputDialog(this).show()
                //AlertDialog.Builder(this).show()
        }
        Log.e(TAG, "onCreate: screen size ${getScreenSize()}")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupSystemUi()
        Log.e(TAG, "onConfigurationChanged: ", )
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setupSystemUi()
        Log.e(TAG, "onWindowFocusChanged: ")
    }

    private fun setupSystemUi() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            SystemUiCompatUtils.immersionMode(window)
        } else {
            SystemUiCompatUtils.showStatusBar(window)
            SystemUiCompatUtils.statusBarColor(window, Color.RED)
            SystemUiCompatUtils.showNavigationBar(window)
            SystemUiCompatUtils.navigationBarColor(window, Color.WHITE)
            WindowCompat.setDecorFitsSystemWindows(window, true)
        }
        Log.e(TAG, "setupSystemUi: statusBarShow: ${BarUtils.isStatusBarVisible(this)} statusBarHeight: ${BarUtils.getStatusBarHeight()}", )
        Log.e(TAG, "setupSystemUi: navigationBarShow: ${BarUtils.isNavBarVisible(this)} navBarHeight: ${BarUtils.getNavBarHeight()}", )
    }

    private fun setupSystemUiWithImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init()
        } else {
            ImmersionBar.with(this)
                .navigationBarEnable(true)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.teal_200)
                .init()
        }
    }

}