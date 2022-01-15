package com.example.inputpaneldialog

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.WindowInsets
import android.widget.LinearLayout
import androidx.core.view.updatePadding

private const val TAG = "LinearLayoutWrap"

class LinearLayoutWrap @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defaultAttr: Int = 0) :
    LinearLayout(context, attrs, defaultAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        val hSize = MeasureSpec.getSize(heightMeasureSpec)
        Log.e(TAG, "onMeasure: mode: $hMode size: $hSize")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


}