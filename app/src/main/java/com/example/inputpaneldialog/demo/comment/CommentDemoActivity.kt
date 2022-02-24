package com.example.inputpaneldialog.demo.comment

import android.os.Bundle
import android.view.View
import android.graphics.Color
import android.content.res.Configuration
import android.net.Uri
import android.view.WindowManager
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.candy.keyboard_aware.KeyboardDialog
import com.candy.keyboard_aware.utils.SystemUiUtils
import com.example.inputpaneldialog.R
import com.example.inputpaneldialog.helper.OrientationHelper

private const val TAG = "MainActivityTag"

class KeyboardDialogDemoActivity : AppCompatActivity() {

    private var panelDialog: KeyboardDialog? = null

    private val orientationHelper by lazy {
        OrientationHelper(this) {
            requestedOrientation = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSystemUi()
        setContentView(R.layout.activity_main_pt)
        initView()
        orientationHelper.start()
    }

    private fun initView() {
        findViewById<View>(R.id.btnShowDialog).setOnClickListener {
            showInputDialog()
        }
        val videoView = findViewById<VideoView>(R.id.vMedia)
        videoView.setVideoURI(Uri.parse("android.resource://$packageName/${R.raw.king}"))
        videoView.requestFocus()
        videoView.start()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        hideInputDialog()
        setupSystemUi()
        super.onConfigurationChanged(newConfig)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        setupSystemUi()
        super.onWindowFocusChanged(hasFocus)
    }

    private fun showInputDialog() {
        panelDialog = CommentKeyboardDialog(this).apply {
            show()
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
        } else {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            SystemUiUtils.showStatusBar(window)
            SystemUiUtils.statusBarColor(window, Color.BLACK)
            SystemUiUtils.showNavigationBar(window)
            SystemUiUtils.navigationBarColor(window, ContextCompat.getColor(this, R.color.black_60))
        }
    }

    override fun onDestroy() {
        orientationHelper.release()
        super.onDestroy()
    }

}