package com.example.inputpaneldialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.graphics.Color
import android.content.res.Configuration
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.inputpaneldialog.dialog.InputPanelDialog
import com.example.inputpaneldialog.utils.SystemUiUtils
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar

private const val TAG = "MainActivityTag"

class MainActivity : AppCompatActivity() {

    private var panelDialog: InputPanelDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSystemUi()
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btnShowDialog).setOnClickListener {
            showInputDialog()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        hideInputDialog()
        setupSystemUi()
        Log.e(TAG, "onConfigurationChanged: ", )
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setupSystemUi()
        Log.e(TAG, "onWindowFocusChanged: ")
    }

    private fun showInputDialog() {
        panelDialog = InputPanelDialog(this).apply { show() }
    }

    private fun hideInputDialog() {
        panelDialog?.hide()
        panelDialog = null
    }

    private fun setupSystemUi() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            SystemUiUtils.immersionMode(window)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            SystemUiUtils.showStatusBar(window)
            SystemUiUtils.statusBarColor(window, Color.RED)
            SystemUiUtils.showNavigationBar(window)
            SystemUiUtils.navigationBarColor(window, Color.WHITE)
            //WindowCompat.setDecorFitsSystemWindows(window, true)
        }
    }

    private fun setupSystemUiWithImmersionBar() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init()
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            ImmersionBar.with(this)
                .navigationBarEnable(true)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.teal_200)
                .init()
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

    }

}