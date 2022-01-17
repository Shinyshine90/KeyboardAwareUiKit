package com.example.inputpaneldialog.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import com.example.inputpaneldialog.R
import com.example.inputpaneldialog.utils.BarUtils
import com.example.inputpaneldialog.utils.ScreenUtils
import com.example.inputpaneldialog.utils.SystemUiUtils
import kotlin.math.abs

private const val TAG = "InputDialog"

/**
 * 输入模块 Dialog 实现类
 * 软键盘的功能模块实现从Activity的Window中抽离
 *
 * function checked from api 21 -> api 31
 *
 * 构造方法中传入一个空配置的主题，不能传入0，否则系统可能会应用一个默认的主题，会影响OwnerActivity样式
 */
class InputPanelDialog(private val activity: Activity) : Dialog(activity, R.style.InputDialogTheme) {

    private var prevVisibleWindowHeight = 0

    private var keyboardHeight = 0

    private val rootView by lazy {
        findViewById<PanelLinearLayout>(R.id.llRoot)
    }

    private val editText by lazy {
        findViewById<EditText>(R.id.etInput)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_input_panel)
        setupWindow()
        findViewById<View>(R.id.btn_dismiss).setOnClickListener { dismiss() }

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val visibleBound = Rect()
            rootView.getWindowVisibleDisplayFrame(visibleBound)
            handleKeyboard(visibleBound)
            Log.e(
                TAG, "onCreate: bound: $visibleBound visible height: " +
                        "${visibleBound.height()} content height: ${rootView.height}"
            )
        }

        rootView.showKeyboard = {
            window?.apply {
                if (it) {
                    SystemUiUtils.openSoftInput(this, editText)
                } else {
                    SystemUiUtils.hideSoftInput(this, editText)
                }
            }
        }

    }

    override fun show() {
        super.show()
        printSystemUiStatus("show")
        editText.requestFocus()
    }

    override fun dismiss() {
        super.dismiss()
        printSystemUiStatus("dismiss")
        window?.apply {
            SystemUiUtils.hideSoftInput(this, editText)
            editText.clearFocus()
        }
    }


    /**
     * Crucial Here
     * 配置Dialog 窗口的属性，和SystemBar的样式
     */
    private fun setupWindow() {
        val window = window ?: return
        /**
         * Dialog Window的宽度撑满，高度默认为屏幕高度，窗口的gravity设置为Bottom，
         * 如果HostActivity展示状态栏，则在计算高度的时候需要扣减调statusBar的高度，
         * 原因是如果Dialog是全屏的，会影响HostActivity的状态栏
         */
        val windowHeight = ScreenUtils.getScreenHeight(activity) -
                BarUtils.getStatusBarHeight(activity.window.decorView)
        Log.e(TAG, "onCreate: $windowHeight")

        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes.height = windowHeight
        window.attributes.gravity = Gravity.BOTTOM
        //配置SoftInputMode，默认弹出软键盘
        @Suppress("DEPRECATION")
        window.attributes.softInputMode =
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

        //SystemBar 设置为在保持HostActivity配置的同时，展示导航栏
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.WHITE

        //这里不能使用这个Flag，会影响WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE 属性
        //window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        @Suppress("DEPRECATION")
        if (activity.window.decorView.systemUiVisibility
            and View.SYSTEM_UI_FLAG_FULLSCREEN == View.SYSTEM_UI_FLAG_FULLSCREEN
        ) {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        var parent: ViewParent? = rootView
        while (parent != null) {
            (parent as? View)?.fitsSystemWindows = false
            parent = parent.parent
        }
    }

    /**
     * 通过监听Window的Visible Area变化 和 WindowInset Api 判断软键盘是否弹出, 和获取软键盘的高度
     */
    private fun handleKeyboard(bound: Rect) {
        if (prevVisibleWindowHeight > 0 && prevVisibleWindowHeight != bound.height()) {
            val diff = bound.height() - prevVisibleWindowHeight
            if (abs(diff) < 200) return
            Log.e(
                TAG, "handleKeyboard: keyboard expand:${BarUtils.isImeVisible(rootView)} " +
                        "imeHeight:${BarUtils.getImeHeight(rootView)} " +
                        "naviBarHeight:${BarUtils.getNaviBarHeight(rootView)}"
            )
            onKeyboardLayoutChange(diff < 0, height = abs(diff))
        }
        prevVisibleWindowHeight = bound.height()
    }

    private fun onKeyboardLayoutChange(expand: Boolean, height: Int) {
        Log.e(TAG, "onKeyboardLayoutChange: expand:$expand height:$height")
    }

    private fun printSystemUiStatus(stage: String = "") {
        Log.e(TAG, "printSystemUiStatus: ${rootView.fitsSystemWindows}")
        Log.e(TAG, "printSystemUiStatus: screenHeight ${ScreenUtils.getScreenHeight(context)}")
        Log.e(
            TAG, "stage: $stage setupSystemUi: statusBarShow: " +
                    "${BarUtils.isStatusBarVisible(rootView)} statusBarHeight: " +
                    "${BarUtils.getStatusBarHeight(rootView)}"
        )
        Log.e(
            TAG, "stage: $stage setupSystemUi: navigationBarShow: " +
                    "${BarUtils.isStatusBarVisible(rootView)} navBarHeight: " +
                    "${BarUtils.isStatusBarVisible(rootView)}"
        )
    }
}