package com.sutech.photoeditor.base

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sutech.photoeditor.BuildConfig
import com.sutech.photoeditor.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.sutech.ads.AdsController
import com.sutech.photoeditor.util.AppConstant
import com.sutech.photoeditor.view.AppOpenManager

abstract class BaseActivity(private val layout:Int) : AppCompatActivity(){
    protected abstract fun onCreatedView()
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        onCreatedView()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        AdsController.init(
            activity = this,
            isDebug = BuildConfig.DEBUG,
            listAppId = arrayListOf(getString(R.string.admob_app_unit_id)),
            packetName = "com.sutech.photoeditor.collagemaker",
            listPathJson = arrayListOf("admob.json"),
            lifecycle = lifecycle
        )
        openApp()
//        AdsController.getInstance(). isPremium = true
    }

    private fun openApp() {
        AppOpenManager(
            this,
            this,
            lifecycle,
            onShowOpenApp = {

            },
            onCloseOpenApp = {

            }, {
                Log.i("vfvfvfvsdfsdfsdfsdsdf", "${AppConstant.checkInter}")
//                logEvent("Recentapp_tap")})

            })
    }
}