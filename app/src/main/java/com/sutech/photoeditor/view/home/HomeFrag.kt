package com.sutech.photoeditor.view.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.sutech.photoeditor.R
import com.sutech.photoeditor.base.BaseFragment
import com.sutech.photoeditor.database.DataStore
import com.sutech.photoeditor.util.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_home.*


class HomeFrag : BaseFragment(R.layout.fragment_home) {
    private var areGranted = false
    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {requestPermissions ->
        areGranted = requestPermissions.all { it.value }
    }

    override fun onCreatedView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
        }

        if (AppUtil.isIAP|| !isNetworkConnected()) {
            ivRemoveAds.visibility = View.GONE
//            ads.visibility = View.VISIBLE
            adsView.visibility = View.GONE
        } else {
            if (isNetworkConnected()) {
                val layoutAds = LayoutInflater.from(context)
                    .inflate(R.layout.layout_ads_native_home_custom, null)
                showAdsNative("native_home", adsView, layoutAds)
                lifecycle.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_RESUME) {
                            Log.e("TAG", "onStateChanged: " + AppConstant.isShowingOpenAd)
                            adsView?.postDelayed({
                                if (!AppConstant.isShowingOpenAd) {
                                    adsView?.show()
                                }
                            }, 1000)
                        } else if (event == Lifecycle.Event.ON_STOP) {
                            adsView?.inv()
                        }
                    }
                })
            } else {
//
//                ads.visibility = View.VISIBLE
                adsView.visibility = View.GONE
            }
        }

        initOnClick()
    }

    override fun onResume() {
        super.onResume()
        if (!DataStore.checkNotFirstOpen() && AppUtil.isShowRateHome == 1) {

            logEvent("DialogRate_Show")
            AppUtil.isShowRateHome = 2
            context?.let { ctx ->
                DialogUtil.showDialogRate(ctx,
                    {
                        logEvent("DialogRate_IconBad_Clicked")
                    },
                    {
                        logEvent("DialogRate_IconGood_Clicked")
                    },
                    {
                        logEvent("DialogRater_IconExcellent_Clicked")
                    },
                    {
                        logEvent("DialogRate_NoThankyou_Clicked")
                    },
                    {
                        DataStore.setNotFirstOpen(true)
                        logEvent("DialogRate_GotoGP_Clicked")
                    },
                    {
                        logEvent("DialogRate_Feedbacktous_Clicked")
                        DataStore.setNotFirstOpen(true)
                    },
                    {
                        logEvent("DialogRate_Mailtous_Clicked")
                        DataStore.setNotFirstOpen(true)
                    },
                    {
                        logEvent("DialogRate_IconX_Clicked")
                    }
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    activity!!.finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            when (requestCode) {
                100 -> {
                    gotoCollage()
                }
                200 -> {
                    gotoEdit()
                }
                300 -> {
                    gotoSaved()
                }
            }
        } else {
            Toast.makeText(
                context,
                R.string.grant_permission,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}