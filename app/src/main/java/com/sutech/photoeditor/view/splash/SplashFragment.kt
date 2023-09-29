package com.sutech.photoeditor.view.splash

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.sutech.ads.AdCallback
import com.sutech.ads.AdsController
import com.sutech.photoeditor.R
import com.sutech.photoeditor.base.BaseFragment
import com.sutech.photoeditor.util.AppConstant
import com.sutech.photoeditor.util.AppUtil
import com.sutech.photoeditor.util.setPreventDoubleClick
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SplashFragment : BaseFragment(R.layout.fragment_splash), PurchasesUpdatedListener {
    var duration = 8000L
    override fun onDestroyView() {
        super.onDestroyView()
        AppConstant.isSplash = false
    }
    override fun onCreatedView() {
        AppConstant.isSplash = true
        activity?.onBackPressedDispatcher?.addCallback(this, true) { }
        progressCustoom.setMax(100)

        if (AdsController.getInstance().isPremium) {
            duration = 3000
        }

        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    count += 1
                    if (count < 100) {
                        progressCustoom?.setProgress(count)
                    }
                    handler.postDelayed(this, duration / 100)
                } catch (e: Exception) {
                }
            }

        }, 0)
//        loadAd()
        checkIAP()
        logEvent("SplashScr_Show")
        imgLogo?.setPreventDoubleClick {
            logEvent("AppStartup_IconApp_Clicked")
        }
    }

    var count = 1
    val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private fun checkIAP() {
        AppUtil.isIAP = false
        activity?.let {
            IapCommon.init(it) {
                CoroutineScope(Dispatchers.Main).launch {
                    setupIAP()
                }
            }
        }
    }

    private fun setupIAP() {
        if (AppUtil.isIAP) {
            navHome()
            if (progressCustoom != null) {
                progressCustoom.setProgress(100)
            }
        } else {
            loadAd()

        }
    }

    private fun loadAd() {
        AppConstant.checkInter = true
        if (findNavController().currentDestination!!.id == R.id.splashFragment) {
            AdsController.getInstance().loadAndShow(
                spaceName = "splash_open",
                timeMillisecond = 9000,
                loadingText = null,
                lifecycle = lifecycle,
                adCallback = object : AdCallback {
                    override fun onAdShow(network: String, adtype: String) {
                        AppConstant.checkInter = true
                        context?.let {
                            progressCustoom?.setProgress(100)
                            handler.removeCallbacksAndMessages(null)
                        }
                    }

                    override fun onAdClick() {
                        super.onAdClick()
                        AppConstant.checkInter = true
                    }
                    override fun onAdClose(adType: String) {
                        AppConstant.checkInter = true
                        Log.e("TAG", "onAdClose: splash " )
                        navHome()
                    }

                    override fun onAdFailToLoad(messageError: String?) {
                        AppConstant.checkInter = true
                        navHome()
                        context?.let {
                            progressCustoom?.setProgress(100)
                            handler.removeCallbacksAndMessages(null)
                        }
                    }

                    override fun onAdOff() {
                        AppConstant.checkInter = true
                        onAdOff()
                    }

                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (isGoHome) {
            gotoFrag(R.id.splashFragment, R.id.action_splashFragment_to_homeFrag)
        }
    }

    var isGoHome = false
    private fun navHome() {
        gotoFrag(R.id.splashFragment, R.id.action_splashFragment_to_homeFrag)
        isGoHome = true

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)

    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {

    }
}