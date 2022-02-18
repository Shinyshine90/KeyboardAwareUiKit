package com.example.inputpaneldialog.helper

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inputpaneldialog.R


fun bindChatView(recyclerView: RecyclerView) {
    val layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.itemAnimator = null
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = ChatAdapter()
}

class ChatAdapter:RecyclerView.Adapter<ChatAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_chat, parent, false)
        return Holder(rootView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (holder.ivAvatar.drawable == null) {
            val resource = holder.ivAvatar.resources
            val avatar = RoundedBitmapDrawableFactory.create(resource, BitmapFactory.decodeResource(resource, R.drawable.ic_avatar_gosling))
            avatar.isCircular = true
            holder.ivAvatar.setImageDrawable(avatar)
        }
        holder.tvContent.text = "I'm the god of Java! $position"
    }

    override fun getItemCount(): Int = 100

    class Holder(root:View): RecyclerView.ViewHolder(root) {

        val ivAvatar = root.findViewById<ImageView>(R.id.ic_avatar)

        val tvContent = root.findViewById<TextView>(R.id.tv_content)

    }
}