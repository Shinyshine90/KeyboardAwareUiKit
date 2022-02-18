package com.example.inputpaneldialog.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

private const val TAG = "LayoutAwareRecyclerView"

class LayoutAwareRecyclerView constructor(context: Context, attrs: AttributeSet): RecyclerView(context, attrs) {

    private var lastPosition = 0

    private var lastOffset = 0


    init {
        addOnScrollListener(object :OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE && layoutManager is LinearLayoutManager) {
                    (layoutManager as? LinearLayoutManager)?.apply {
                        lastPosition = this.findLastVisibleItemPosition()
                        lastOffset = this.findViewByPosition(lastPosition)?.top ?: 0
                    }

                }
            }
        })
    }

    fun handleLayoutChange(height: Int) {
        if (scrollState != SCROLL_STATE_IDLE) {
            return
        }
        //(layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(lastPosition, lastOffset - height)
    }


}