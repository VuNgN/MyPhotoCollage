package com.sutech.photoeditor.base


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sutech.photoeditor.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.sutech.ads.AdCallback
import com.sutech.ads.AdsController
import com.sutech.photoeditor.util.AppConstant
import com.sutech.photoeditor.util.AppUtil

abstract class BaseFragment(private val layout: Int) : Fragment() {


    val logger: FirebaseAnalytics by lazy{
        Firebase.analytics
    }
  fun   isNetworkConnected(): Boolean {
        val cm =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
    override fun onResume() {
        super.onResume()
        if (!AppConstant.isSplash){
            AppConstant.checkInter = false
        }
    }
    protected abstract fun onCreatedView()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreatedView()
    }


    public fun gotoFrag(currentId: Int, action: Int, bundle: Bundle? = null) {
        try {
            if (findNavController().currentDestination!!.id == currentId) {
                findNavController().navigate(action, bundle)
            }
        } catch (e: Exception) {

        }
    }

    public fun onBackToHome() {
        try {
            findNavController().popBackStack(R.id.homeFrag, false)
        } catch (e: Exception) {

        }
    }

    public fun onBackPress() {

        try {
            findNavController().popBackStack()
        } catch (e: Exception) {

        }
    }

    public fun onBackFinish() {
        try {
            activity?.onBackPressedDispatcher?.addCallback(this, true) {
                activity?.finish()
            }
        } catch (e: Exception) {

        }
    }

    fun showAdInter(
        spaceName: String,
        timeOut: Long,
        loadingText: String?,
        onAdShow: () -> Unit,
        onAdClose: () -> Unit,
        onAdFailToLoad: () -> Unit,
        onAdOff: () -> Unit
    ) {

        AdsController.getInstance().loadAndShow(
            spaceName = spaceName,
            timeMillisecond = timeOut,
            loadingText = loadingText,
            lifecycle = lifecycle,
            adCallback = object : AdCallback {
                override fun onAdShow(network: String, adtype: String) {
                    AppConstant.checkInter = true
                    onAdShow()
                }

                override fun onAdClose(adType: String) {
                    AppConstant.checkInter = true
                    onAdClose()
                }

                override fun onAdFailToLoad(messageError: String?) {
                    AppConstant.checkInter = true
                    onAdFailToLoad()
                }

                override fun onAdOff() {
                    AppConstant.checkInter = true
                    onAdOff()
                }

                override fun onAdClick() {
                    AppConstant.checkInter = true
                    super.onAdClick()

                }

            }
        )
    }

    fun showAdsNative(
        spaceName: String,
        viewGroup: ViewGroup,
        view: View,
    ) {

//            logParams("$spaceName" + "_Ads_Param") {
//                param("Request_Check", 1)
//            }
        AdsController.getInstance()
            .loadAndShow(spaceName,
                layout = viewGroup,
                layoutAds = view,
                adCallback = object : AdCallback {
                    override fun onAdShow(network: String, adtype: String) {
//                            logParams("$spaceName" + "_Ads_Param") {
//                                param("Show_Check", 1)
//                            }
                    }

                    override fun onAdClose(adType: String) {

                    }

                    override fun onAdFailToLoad(messageError: String?) {
                        viewGroup.visibility = View.GONE
                    }

                    override fun onAdOff() {

                    }

                    override fun onAdClick() {
                        super.onAdClick()
                        AppConstant.checkInter = true
                    }

                })


    }


    fun showBanner(
        spaceName: String,
        viewGroup: ViewGroup,
        view:View
    ) {
        AdsController.getInstance().loadAndShow(
            spaceName = spaceName,
            layout = viewGroup,
            adCallback = object : AdCallback {
                override fun onAdShow(network: String, adtype: String) {
                }

                override fun onAdClose(adType: String) {
                }

                override fun onAdFailToLoad(messageError: String?) {
                    viewGroup.visibility = View.GONE
                    view.visibility = View.VISIBLE
                }

                override fun onAdOff() {

                }

                override fun onAdClick() {
                    super.onAdClick()

                }

            }
        )

    }


    fun logParams(trackingName: String, block: ParametersBuilder.() -> Unit) {
        logger.logEvent(trackingName.trim().replace(" ", "")) {
            block()
        }
    }

    fun logEventScreen(screenName: String) {
        try {
            activity?.let {
                logger.setCurrentScreen(
                    it,
                    screenName.trim().replace(" ", ""),
                    screenName
                )
            }
        } catch (e: Exception) {

        }
    }

    fun logEvent(eventName: String) {
        logger.logEvent(eventName, Bundle.EMPTY)
    }
}