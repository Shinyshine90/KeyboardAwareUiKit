package com.example.inputpaneldialog.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "LayoutAwareRecyclerView"

class LayoutAwareRecyclerView constructor(context: Context, attrs: AttributeSet):
    RecyclerView(context, attrs) {

    private var lastHeight = 0

    init {
        clipToPadding = false
    }

    fun handleBottomHeight(height: Int) {
        Log.e(TAG, "on global layout: $lastHeight $height")
        val diff = height - lastHeight
        if (lastHeight > 0 && diff != 0) {
            smoothScrollBy(0, diff, LinearInterpolator(), 200)
        }
        lastHeight = height
    }

}