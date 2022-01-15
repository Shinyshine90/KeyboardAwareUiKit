package com.example.inputpaneldialog.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import com.blankj.utilcode.util.BarUtils
import com.example.inputpaneldialog.R
import com.example.inputpaneldialog.utils.SystemUiCompatUtils
import com.example.inputpaneldialog.utils.getScreenHeight
import kotlin.math.abs

private const val TAG = "InputDialog"

class InputDialog(private val activity: Activity): Dialog(activity, R.style.InputDialogTheme){

    init {
        //SystemUiCompatUtils.decorFitSystemWindow(window!!, false)
        //window?.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //这里主要是为了处理Dialog弹出时，对Activity StatusBar的影响
        //window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        setOnShowListener {
            //这里同步activity的decorView对系统Ui的配置
            //window?.decorView?.systemUiVisibility = activity.window.decorView.systemUiVisibility
            //window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }
    }

    private var init = false

    private var prevRect = Rect()

    private var prevVisibleHeight = 0

    private var keyboardHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes?.height = activity.getScreenHeight() - getVisibleStatusBarHeight() - getVisibleNavBarHeight()
        window?.attributes?.gravity = Gravity.BOTTOM
        window?.attributes?.softInputMode =
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

        setContentView(R.layout.dialog_input_panel)
        findViewById<View>(R.id.btn_dismiss).setOnClickListener { dismiss() }
        findViewById<View>(R.id.et_input).setOnClickListener {
            it.requestFocus()
        }

        val rootView = findViewById<View>(R.id.llRoot)

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val visibleBound = Rect()
            rootView.getWindowVisibleDisplayFrame(visibleBound)
            initWindowHeight(visibleBound)
            handleKeyboard(visibleBound)
            Log.e(TAG, "onCreate: bound: $visibleBound visible height: ${visibleBound.height()} content height: ${rootView.height}")
        }

    }


    override fun show() {
        super.show()
        printSystemUiStatus("show")
    }

    override fun dismiss() {
        window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        super.dismiss()
        printSystemUiStatus("dismiss")
    }

    private fun handleKeyboard(bound: Rect) {
        if (prevVisibleHeight > 0 && prevVisibleHeight != bound.height()) {
            val diff = bound.height() - prevVisibleHeight
            if (abs(diff) < 260) return
            onKeyboardLayoutChange(diff < 0)
        }
        prevVisibleHeight = bound.height()
    }

    private fun onKeyboardLayoutChange(expand: Boolean) {
        Log.e(TAG, "onKeyboardLayoutChange: $expand" )
    }

    private fun initWindowHeight(bound: Rect) {
        if (init) return
        val hasStatusBar = BarUtils.isStatusBarVisible(activity)
        if (!hasStatusBar || bound.top > 0) {
            init = true
            window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, bound.height())
        }
    }

    private fun getVisibleStatusBarHeight() =
        if (BarUtils.isStatusBarVisible(activity)) {
            BarUtils.getStatusBarHeight()
        } else 0

    private fun getVisibleNavBarHeight() =
        if (BarUtils.isNavBarVisible(activity)) {
            BarUtils.getNavBarHeight()
        } else 0

    private fun printSystemUiStatus(stage: String = "") {
        Log.e(TAG, "printSystemUiStatus: ${window?.decorView?.fitsSystemWindows}")
        Log.e(TAG, "printSystemUiStatus: screenHeight ${activity.getScreenHeight()}")
        Log.e(TAG, "stage: $stage setupSystemUi: statusBarShow: ${BarUtils.isStatusBarVisible(activity)} statusBarHeight: ${BarUtils.getStatusBarHeight()}", )
        Log.e(TAG, "stage: $stage setupSystemUi: navigationBarShow: ${BarUtils.isNavBarVisible(activity)} navBarHeight: ${BarUtils.getNavBarHeight()}", )
    }
}