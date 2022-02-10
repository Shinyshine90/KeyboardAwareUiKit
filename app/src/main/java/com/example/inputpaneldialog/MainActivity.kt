package com.example.inputpaneldialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.graphics.Color
import android.content.res.Configuration
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import com.candy.kdialog.KeyboardDialog
import com.candy.kdialog.utils.SystemUiUtils
import com.example.inputpaneldialog.dialog.DemoKeyboardDialog
import com.example.inputpaneldialog.helper.OrientationHelper

private const val TAG = "MainActivityTag"

class MainActivity : AppCompatActivity() {

    private var panelDialog: KeyboardDialog? = null

    private val btnShowDialog by lazy {
        findViewById<View>(R.id.btnShowDialog)
    }

    private val orientationHelper by lazy {
        OrientationHelper(this) {
            requestedOrientation = it
        }
    }

    private val tvContent by lazy {
        findViewById<View>(R.id.tvContent)
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
        panelDialog = DemoKeyboardDialog(this).apply {
            show {
                registerBottomUiHeightChanged {
                    val bottom = (it - btnShowDialog.height).coerceAtLeast(0)
                    tvContent.updatePadding(bottom = bottom)
                }
            }
        }
    }

    private fun hideInputDialog() {
        panelDialog?.dismiss()
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