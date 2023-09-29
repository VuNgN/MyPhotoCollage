package com.sutech.photoeditor.view

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.sutech.ads.AdsController
import com.sutech.photoeditor.BuildConfig
import com.sutech.photoeditor.util.AppConstant.*
import com.sutech.photoeditor.util.AppUtil

class AppOpenManager(
    val context: Context,
    val activity: Activity,
    lifecycle: Lifecycle,
    val onShowOpenApp: () -> Unit,
    val onCloseOpenApp: () -> Unit,
    val onRecent: () -> Unit
) {
    private val AD_UNIT_ID = "ca-app-pub-2094788208346877/1494359198"
    private val AD_UNIT_ID_DEBUG = "ca-app-pub-3940256099942544/3419835294"
    var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAd.AppOpenAdLoadCallback? = null


    init {
        lifecycle.addObserver(LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    Log.e("TAG", "AppOpenManager: ON_START" )
                    showAdIfAvailable()
                }

                Lifecycle.Event.ON_DESTROY -> {
                    appOpenAd = null
                }

                else -> {

                }
            }
        })
    }

    private fun showAdIfAvailable() {
        Log.i("fsdfsdfsdfsdfsdsd", "$checkInter")
        Log.i("fsdfsdfsdfsdfsdsd", "$showOpenApp")
        if (!isShowingOpenAd &&!AppUtil.isIAP && isAdAvailable() && !checkInter && showOpenApp) {
            onRecent()
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        appOpenAd = null
                        isShowingOpenAd = false
                        onCloseOpenApp()
                        checkInter = true
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        if (AdsController.getInstance().getDebugMode()) {
                            Toast.makeText(
                                context,
                                AD_UNIT_ID,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onAdShowedFullScreenContent() {
                        if (BuildConfig.DEBUG) {
                            Toast.makeText(
                                context,
                                AD_UNIT_ID,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        checkInter = true
                        onShowOpenApp()
                        isShowingOpenAd = true
                    }

                    override fun onAdClicked() {
                        if (AdsController.mTopActivity != null && AdsController.mTopActivity is AdActivity) {
//                            AdsController.mTopActivity?.finish()
                        }
                    }
                }
            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd?.show(activity)
        } else {
            fetchAd()
        }
    }
    fun fetchAd() {
        if (isAdAvailable() && AppUtil.isIAP) {
            return
        }
        Log.e("TAGGGG", "fetchAd: vaoooooo" )

        activity.application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {
//                AdsController.mTopActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.e("TAG", "onActivityDestroyed: " )
//                AdsController.mTopActivity = null
            }
        })
        loadCallback =
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    super.onAdLoaded(p0)
                    appOpenAd = p0
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    fetchAd()
                }
            }

        val request = getAdRequest()

        if (AdsController.getInstance().getDebugMode()) {
            AppOpenAd.load(
                context,
                AD_UNIT_ID_DEBUG,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                loadCallback as AppOpenAd.AppOpenAdLoadCallback
            )
        } else {
            AppOpenAd.load(
                context,
                AD_UNIT_ID,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                loadCallback as AppOpenAd.AppOpenAdLoadCallback
            )
        }
    }

    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }

}














