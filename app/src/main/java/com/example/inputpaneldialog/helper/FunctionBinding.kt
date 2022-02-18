package com.example.inputpaneldialog.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inputpaneldialog.R


fun bindFunction(recyclerView: RecyclerView) {
    recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 4)
    recyclerView.adapter = FunctionAdapter()
}

class FunctionAdapter:RecyclerView.Adapter<FunctionAdapter.Holder>() {

    private val funcMap = listOf(
        "相册" to R.drawable.ic_func_album,
        "拍摄" to R.drawable.ic_func_camera,
        "视频通话" to R.drawable.ic_func_video,
        "位置" to R.drawable.ic_func_locate,
        "红包" to R.drawable.ic_func_red_en,
        "转账" to R.drawable.ic_func_transform,
        "语音输入" to R.drawable.ic_func_voice,
        "收藏" to R.drawable.ic_func_favor,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_function, parent, false)
        return Holder(rootView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvFunc.text = funcMap[position].first
        holder.ivFunc.setImageResource(funcMap[position].second)
    }

    override fun getItemCount(): Int = funcMap.size

    class Holder(root: View): RecyclerView.ViewHolder(root) {

        val ivFunc = root.findViewById<ImageView>(R.id.iv_func)

        val tvFunc = root.findViewById<TextView>(R.id.tv_func)

    }
}