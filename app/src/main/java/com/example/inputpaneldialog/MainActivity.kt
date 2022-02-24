package com.example.inputpaneldialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.candy.keyboard_aware.utils.SystemUiUtils
import com.example.inputpaneldialog.demo.chat.ChatDemoActivity
import com.example.inputpaneldialog.demo.comment.KeyboardDialogDemoActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SystemUiUtils.statusBarColor(window, ContextCompat.getColor(this, R.color.black_60))
        SystemUiUtils.navigationBarColor(window, ContextCompat.getColor(this, R.color.black_60))

        findViewById<View>(R.id.btn_widget_demo).setOnClickListener {
            startActivity(Intent(this, ChatDemoActivity::class.java))
        }

        findViewById<View>(R.id.btn_dialog_demo).setOnClickListener {
            startActivity(Intent(this, KeyboardDialogDemoActivity::class.java))
        }
    }
}