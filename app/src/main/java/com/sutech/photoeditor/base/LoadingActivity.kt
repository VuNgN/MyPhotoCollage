package com.sutech.photoeditor.base

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.sutech.photoeditor.R

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        onBackPressedDispatcher.addCallback(this, true) { }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}