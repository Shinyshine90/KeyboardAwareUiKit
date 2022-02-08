package com.example.inputpaneldialog.helper

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.OrientationEventListener

private const val TAG = "OrientationHelper"

private const val WHAT_MSG = 0x1024

class OrientationHelper(private val context: Context, private val orientationChanged: ((Int) -> Unit)? = null) {

    var orientation = 0
        private set

    @Volatile
    private var pendingOrientation = 0


    private val handler = object: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.obj == pendingOrientation) {
                onOrientationChanged(msg.obj as Int)
            }
        }
    }

    private val eventListener = object : OrientationEventListener(context) {

        private val LOOKUP = arrayOf(
            0, 0, 0,
            90, 90, 90, 90, 90, 90,
            180, 180, 180, 180, 180, 180,
            270, 270, 270, 270, 270, 270,
            0, 0, 0)

        override fun onOrientationChanged(orientation: Int) {
            if (orientation == ORIENTATION_UNKNOWN) return
            pendingOrientation = LOOKUP[orientation / 15]
            if (pendingOrientation == this@OrientationHelper.orientation) return
            val checkMsg =  handler.obtainMessage(WHAT_MSG, pendingOrientation)
            handler.sendMessageDelayed(checkMsg, 200L)
        }
    }

    fun start() {
        eventListener.enable()
    }

    fun release() {
        eventListener.disable()
        handler.removeMessages(WHAT_MSG)
    }

    private fun onOrientationChanged(orientation: Int) {
        Log.e(TAG, "onOrientationChanged: $orientation")
        this.orientation = orientation
        handler.removeMessages(WHAT_MSG)

        orientationChanged?.apply {
            val config = when (orientation) {
                0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                90 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                else -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            invoke(config)
        }
    }


}