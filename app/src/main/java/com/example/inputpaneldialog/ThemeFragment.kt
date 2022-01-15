package com.example.inputpaneldialog

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

private const val TAG = "ThemeFragment"

class ThemeFragment: Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(TAG, "onAttach: ", )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: ", )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG, "onCreateView: ", )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: ", )
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: ", )
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop: ", )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
    
}