package com.sutech.photoeditor.widget.text.database

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(k1: Context, k2: Intent) {
        Log.e("TAG", "onReceive: " )
        Toast.makeText(k1, "Alarm received! ${k2.getStringExtra("content")}", Toast.LENGTH_LONG).show()
    }
}