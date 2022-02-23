package com.example.inputpaneldialog.demo.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.inputpaneldialog.R
import com.example.inputpaneldialog.helper.bindChatView
import com.example.inputpaneldialog.helper.bindEmoji
import com.example.inputpaneldialog.helper.bindFunction

private const val TAG = "ChatActivity"

class ChatDemoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contentView = ChatKeyboardLayout(this)
        setContentView(contentView)

        bindChatView(findViewById(R.id.rv_chat))

        bindEmoji(contentView.emojiPanel)

        bindFunction(contentView.functionPanel.findViewById(R.id.rv_function))
    }

}