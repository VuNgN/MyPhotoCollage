package com.sutech.photoeditor.view.splash


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.android.billingclient.api.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.sutech.ads.AdsController
import com.sutech.photoeditor.util.AppUtil
import kotlinx.coroutines.*

@SuppressLint("StaticFieldLeak")
object IapCommon {
    var billingClient: BillingClient? = null
    var params = SkuDetailsParams.newBuilder()
    var skuList: ArrayList<String> = ArrayList()
    var activity: Activity? = null

    var nameTracking: String = ""
    var valueCode = ""

    fun init(activity: Activity, onDone: () -> Unit) {
        this.activity = activity
        billingClient = BillingClient.newBuilder(activity)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    var list :MutableList<Purchase>?=null
                        billingClient?.queryPurchasesAsync(BillingClient.SkuType.INAPP,object :PurchasesResponseListener{
                            override fun onQueryPurchasesResponse(
                                p0: BillingResult,
                                p1: MutableList<Purchase>
                            ) {
                                list =  p1
                                if (!list.isNullOrEmpty()  ) {
                                    AppUtil.isIAP = true
                                    AdsController.getInstance().isPremium = true
                                } else {
                                    AdsController.getInstance().isPremium = false
                                    AppUtil.isIAP = false
                                }
                                onDone()
                            }
                        })

//                      billingClient?.queryPurchasesAsync(BillingClient.SkuType.SUBS,object :PurchasesResponseListener{
//                        override fun onQueryPurchasesResponse(
//                            p0: BillingResult,
//                            p1: MutableList<Purchase>
//                        ) {
//                            listMonth =     p1
//                        }
//                    })

                    querySkuDetails()
                }
            }

            override fun onBillingServiceDisconnected() {
                onDone()
            }
        })
    }

    fun querySkuDetails() {
        CoroutineScope(Dispatchers.Default).launch {
            val promise = async {
//                checkSkuMonth()
            }
            promise.await()
            checkSkuForever()
        }
    }

    private fun checkSkuForever() {
        skuList.add(AppUtil.SKU_FOREVER)
        skuList.add(AppUtil.SKU_FOREVER_FAKE)
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        billingClient?.querySkuDetailsAsync(
            params.build()
        ) { billingResult, skuDetailsList ->
            skuDetailsList?.forEach {
                if (it.sku == AppUtil.SKU_FOREVER) {
                    AppUtil.PRICE_FOREVER = it.price.toString()
                }
                if (it.sku == AppUtil.SKU_FOREVER_FAKE) {
                    AppUtil.PRICE_FOREVER_FAKE = it.price.toString()
                }
            }
        }
    }

    fun buyIap(id: String) {

        val type = if (id == AppUtil.SKU_FOREVER) {
            BillingClient.SkuType.INAPP
        } else BillingClient.SkuType.SUBS

        params.setSkusList(skuList).setType(type)
        billingClient?.querySkuDetailsAsync(params.build(),
            SkuDetailsResponseListener { billingResult, skuDetailsList ->
                skuDetailsList?.let {
                    if (it.size > 0) {
                        for (item in it) {
                            if (item.sku == id) {
                                val billingFlowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(item)
                                    .build()
                                val responseCode: Int? = activity?.let { it1 ->
                                    billingClient?.launchBillingFlow(
                                        it1, billingFlowParams
                                    )?.responseCode
                                }
                                break
                            }
                        }
                    }
                }
            })
    }


    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            }
        }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                val ackPurchaseResult = CoroutineScope(Dispatchers.Default).launch {
                    withContext(Dispatchers.IO) {
                        billingClient?.acknowledgePurchase(
                            acknowledgePurchaseParams.build()
                        ) { p0 ->
                            if (p0.responseCode == BillingClient.BillingResponseCode.OK) {
                                AppUtil.isIAP = true
                                restart()
                            }
                        }
                    }
                }
            }
        }
    }


    fun restart() {
        //  trackingBuySuccess()
        val intent: Intent? = activity?.packageName?.let {
            activity?.packageManager
                ?.getLaunchIntentForPackage(it)
        }
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity?.startActivity(intent)
        activity?.finish()
    }

    fun resetIap() {
        try {
            val listForever = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP).purchasesList
            val listMonth = billingClient!!.queryPurchases(BillingClient.SkuType.SUBS).purchasesList

            if (!listForever.isNullOrEmpty()) {
                for (item in listForever) {
                    val consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(item.purchaseToken)
                        .build()
                    billingClient!!.consumeAsync(
                        consumeParams
                    ) { billingResult, s ->
                        AppUtil.isIAP = false
                    }

                }
            }

            if (!listMonth.isNullOrEmpty()) {
                for (item in listMonth) {
                    val consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(item.purchaseToken)
                        .build()
                    billingClient!!.consumeAsync(
                        consumeParams
                    ) { billingResult, s ->
                        AppUtil.isIAP = false
                    }
                }
            }

        } catch (e: java.lang.Exception) {

        }
    }

}
