package com.example.inputpaneldialog.transition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.example.inputpaneldialog.R

class TransitionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)

        val container = findViewById<ViewGroup>(R.id.container)
        val target = findViewById<View>(R.id.target)

        findViewById<View>(R.id.add).setOnClickListener {
            val transition = ChangeBounds()
            transition.addTarget(target)
            transition.duration = 200
            TransitionManager.beginDelayedTransition(container, transition)
            target.updateLayoutParams {
                height += 200
            }
        }

    }
}