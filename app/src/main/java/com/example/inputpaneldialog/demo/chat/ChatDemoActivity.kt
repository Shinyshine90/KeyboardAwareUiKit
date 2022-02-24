package com.example.inputpaneldialog.demo.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.candy.keyboard_aware.utils.SystemUiUtils
import com.example.inputpaneldialog.R
import com.example.inputpaneldialog.helper.bindChatView
import com.example.inputpaneldialog.helper.bindEmoji
import com.example.inputpaneldialog.helper.bindFunction

private const val TAG = "ChatActivity"

class ChatDemoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUiUtils.statusBarColor(window, ContextCompat.getColor(this, R.color.black_60))
        val contentView = ChatKeyboardLayout(this)
        setContentView(contentView)

        bindChatView(findViewById(R.id.rv_chat))

        bindEmoji(contentView.emojiPanel)

        bindFunction(contentView.functionPanel.findViewById(R.id.rv_function))
    }

}