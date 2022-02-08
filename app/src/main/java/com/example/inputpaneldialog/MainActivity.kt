package com.example.inputpaneldialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.graphics.Color
import android.content.res.Configuration
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.candy.kdialog.InputPanelDialog
import com.candy.kdialog.utils.SystemUiUtils
import com.example.inputpaneldialog.helper.OrientationHelper

private const val TAG = "MainActivityTag"

class MainActivity : AppCompatActivity() {

    private var panelDialog: InputPanelDialog? = null

    private val orientationHelper by lazy {
        OrientationHelper(this) {
            requestedOrientation = it
        }
    }

    private val sceneRoot by lazy {
        findViewById<ViewGroup>(R.id.flRoot)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: ")
        setupSystemUi()
        setContentView(R.layout.activity_main)
        initView()
        orientationHelper.start()
    }

    private fun initView() {
        findViewById<View>(R.id.btnShowDialog).setOnClickListener {
            showInputDialog()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        hideInputDialog()
        setupSystemUi()
        super.onConfigurationChanged(newConfig)
        Log.e(TAG, "onConfigurationChanged: ")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        setupSystemUi()
        super.onWindowFocusChanged(hasFocus)
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
            //SystemUiUtils.hideStatusBar(window)
            //SystemUiUtils.hideNavigationBar(window)
        } else {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            SystemUiUtils.showStatusBar(window)
            SystemUiUtils.statusBarColor(window, Color.RED)
            SystemUiUtils.showNavigationBar(window)
            SystemUiUtils.navigationBarColor(window, Color.WHITE)
        }
    }

    override fun onDestroy() {
        orientationHelper.release()
        super.onDestroy()
    }

}