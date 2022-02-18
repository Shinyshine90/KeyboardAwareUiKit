package com.example.inputpaneldialog.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inputpaneldialog.R


fun bindEmoji(recyclerView: RecyclerView) {
    recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 7)
    recyclerView.adapter = EmojiAdapter()
}

class EmojiAdapter:RecyclerView.Adapter<EmojiAdapter.Holder>() {

    private val emojiList = MutableList(100) { "\uD83D\uDE01" }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_emoji, parent, false)
        return Holder(rootView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvEmoji.text = emojiList[position]
    }

    override fun getItemCount(): Int = emojiList.size

    class Holder(root: View): RecyclerView.ViewHolder(root) {

        val tvEmoji = root.findViewById<TextView>(R.id.tv_emoji)

    }
}