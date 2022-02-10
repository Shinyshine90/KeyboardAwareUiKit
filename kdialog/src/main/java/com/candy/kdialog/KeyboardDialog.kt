package com.candy.kdialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewParent
import android.view.WindowManager
import com.candy.kdialog.entity.KeyboardBottomUi
import com.candy.kdialog.utils.BarUtils
import com.candy.kdialog.utils.ScreenUtils
import com.candy.kdialog.utils.SystemUiUtils
import com.candy.kdialog.widget.KeyboardAwareLinearLayout

private const val TAG = "KeyboardDialog"

abstract class KeyboardDialog(private val activity: Activity) :
    Dialog(activity, R.style.InputDialogTheme) {

    private var onCreateComplete = {}

    /**
     * notify dialog show state
     */
    private val visibleChangesCallbacks = mutableListOf<(Boolean) -> Unit>()

    private val rootView by lazy {
        findViewById<KeyboardAwareLinearLayout>(R.id.llRoot)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_input_panel)
        rootView.setBottomBar(createBottomBar())
        rootView.openSoftInput = ::openSoftInput
        setupWindow()
        onCreateComplete()
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

    private fun openSoftInput(show: Boolean) {
        window?.apply {
            if (show) {
                SystemUiUtils.openSoftInput(this, rootView)
            } else {
                SystemUiUtils.hideSoftInput(this, rootView)
            }
        }
    }

    abstract fun createBottomBar(): KeyboardBottomUi

    fun show(completeCreate: () -> Unit) {
        onCreateComplete = completeCreate
        super.show()
    }

    fun getKeyboardHeight() = rootView.keyboardHeight

    fun getDisplayKeyboardHeight() =
        if (rootView.keyboardExpand) { rootView.keyboardHeight } else 0

    fun getDisplayBottomBarHeight() = rootView.displayBottomUiHeight


    fun registerVisibleChanged(callback: (Boolean) -> Unit) {
        visibleChangesCallbacks.add(callback)
    }

    fun unregisterVisibleChanged(callback: (Boolean) -> Unit) {
        visibleChangesCallbacks.remove(callback)
    }

    fun registerKeyboardChanged(callback: (Boolean) -> Unit) {
        rootView.registerKeyboardChanged(callback)
    }

    fun unregisterKeyboardChanged(callback: (Boolean) -> Unit) {
        rootView.unregisterKeyboardChanged(callback)
    }

    fun registerKeyboardHeightChanged(callback: (Int) -> Unit) {
        rootView.registerKeyboardHeightChanged(callback)
    }

    fun unregisterKeyboardHeightChanged(callback: (Int) -> Unit) {
        rootView.unregisterKeyboardHeightChanged(callback)
    }

    fun registerBottomUiHeightChanged(callback: (Int) -> Unit) {
        rootView.registerBottomUiHeightChanged(callback)
    }

    fun unregisterBottomUiHeightChanged(callback: (Int) -> Unit) {
        rootView.unregisterBottomUiHeightChanged(callback)
    }

}