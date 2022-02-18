package com.example.inputpaneldialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.inputpaneldialog.demo.view.ChatDemoActivity
import com.example.inputpaneldialog.demo.dialog.KeyboardDialogDemoActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn_widget_demo).setOnClickListener {
            startActivity(Intent(this, ChatDemoActivity::class.java))
        }

        findViewById<View>(R.id.btn_dialog_demo).setOnClickListener {
            startActivity(Intent(this, KeyboardDialogDemoActivity::class.java))
        }
    }
}