package com.example.inputpaneldialog.demo.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater

private const val TAG = "ChatActivity"

class ChatDemoActivity : AppCompatActivity() {

    private val rootView by lazy {
        ChatKeyboardLayout(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(rootView)

    }

    private fun inflateView(id: Int) = LayoutInflater.from(this).inflate(id, null)
}