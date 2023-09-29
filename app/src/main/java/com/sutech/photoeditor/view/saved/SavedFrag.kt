package com.sutech.photoeditor.view.saved

import android.content.Context
import android.net.ConnectivityManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sutech.photoeditor.R
import com.sutech.photoeditor.base.BaseFragment
import com.sutech.photoeditor.model.ImageGalleryObj
import com.sutech.photoeditor.util.*
import com.sutech.photoeditor.view.saved.adapter.SavedAdapter
import com.sutech.ads.AdCallback
import com.sutech.ads.AdsController
//import com.volio.ads.AdCallback
//import com.volio.ads.AdsController
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.android.synthetic.main.toolbar_base.*

class SavedFrag : BaseFragment(R.layout.fragment_saved) {
    var positon = 0
    val listImage = mutableListOf<ImageGalleryObj>()

    val adapterSaved by lazy {
        SavedAdapter(listImage) {
            ImageUtil.setImage(ivFull, listImage[it].pathImage)
            layoutFullImage.show()
            positon = it
        }
    }


    override fun onCreatedView() {
        tvTitleToolbar.setText(R.string.save_photo)
        val layoutManager = GridLayoutManager(context,2,RecyclerView.VERTICAL,false)
//        val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        rcvMyCreated?.layoutManager = layoutManager
        rcvMyCreated?.adapter = adapterSaved

        loadImage()
        initClick()
        initOnBackPress()

//        if(AppUtil.isIAP){
//            adsView.visibility = View.GONE
//        }else{
//            if(isNetworkConnected()){
//                val layoutAds =
//                    LayoutInflater.from(context).inflate(R.layout.layout_ads_native_custom, null)
//                showAdsNative2("native_saved", adsView, layoutAds)
//            }else{
//                adsView.visibility = View.GONE
//            }
//        }
        showAds()
    }

    private fun showAds() {
//        AdsController.getInstance().loadAndShow(
//            spaceName = "banner_save",
//            layout = layoutAdsSaved,
//            adCallback = object : AdCallback {
//                override fun onAdClick() {
//                    super.onAdClick()
//                    AppConstant.checkInter = true
//                }
//
//                override fun onAdShow(network: String, adtype: String) {
//                    viewGoneAd?.gone()
//                }
//
//                override fun onAdClose(adType: String) {
//
//                }
//
//                override fun onAdFailToLoad(messageError: String?) {
                    layoutAdsSaved?.gone()
//                }
//
//                override fun onAdOff() {
//
//                }
//
//            })

    }

}