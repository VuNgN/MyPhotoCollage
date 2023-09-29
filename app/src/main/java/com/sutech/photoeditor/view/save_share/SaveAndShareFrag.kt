package com.sutech.photoeditor.view.save_share

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.addCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.sutech.photoeditor.R
import com.sutech.photoeditor.base.BaseFragment
import com.sutech.photoeditor.database.DataStore
import com.sutech.photoeditor.util.*
import com.sutech.photoeditor.util.AppUtil.openMarket
import com.sutech.ads.AdCallback
import com.sutech.ads.AdsController
import kotlinx.android.synthetic.main.fragment_save_share.*

class SaveAndShareFrag : BaseFragment(R.layout.fragment_save_share) {
    var path: Uri? = null
    override fun onCreatedView() {
        logEvent("SavedScr_Show")
        showAds()
        loadImage()
        activity?.onBackPressedDispatcher?.addCallback(this, true) {
            if (viewGone?.visibility == View.VISIBLE) {
                Handler(Looper.myLooper()!!).postDelayed({
                    try {
                        transformationLayout.finishTransform()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }, 200)

            } else {
//                if (!isEditFrame) {
//                    findNavController().popBackStack()
//                } else {
                findNavController().popBackStack(R.id.homeFrag, false)
//                }
//                findNavController().popBackStack(R.id.homeFragment, false)
//            }
            }
            viewGone?.gone()

        }


        btnBackToHome.setPreventDoubleClickItem(1000) {
            logEvent("SavedScr_IconHome_Clicked")
            if (   AppUtil.isShowRateHome ==0){
                AppUtil.isShowRateHome = 1
            }
            findNavController().popBackStack(R.id.homeFrag, false)
        }
        btnIapSaveShare.setPreventDoubleClickItem(1000) {
            try {
                logEvent("SavedScr_IconIAP_Clicked")
                findNavController().navigate(R.id.action_saveAndShareFrag_to_IAPFragment)
            }catch (e:Exception){}
        }
        btnShareSaved.setPreventDoubleClickItem(1000) {
            logEvent("SavedScr_IconShare_Clicked")
            SharePhotorUtils.getInstance().sendShareMore(context, path)
        }

        btnCancelShareSaved?.setPreventDoubleClickScaleView(1000) {
            Handler(Looper.myLooper()!!).postDelayed({
                try {
                    viewGone.gone()
                    transformationLayout.finishTransform()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                viewGone.gone()
            }, 300)

        }

        btnRate?.setPreventDoubleClickScaleView(1000) {
            if (numRate!=0){
                if (numRate < 5) {
                    DialogUtil.showDialogAlert(
                        requireContext(),
                        getString(R.string.message_dialog_feed_back),
                        {
                            DataStore.setNotFirstOpen(true)
                          rlRateApp.inv()
                            AppUtil.sendEmailFeedBack(
                                requireContext(),
                                arrayOf("khoanglang270102@gmail.com"),
                                "Feedback to ${AppConstant.FOLDER_PHOTO_LAYOUT}",
                                ""
                            )
                        },
                        {
                            DataStore.setNotFirstOpen(true)
                          rlRateApp.inv()
                        })
                } else {
//                    DialogUtil.showDialogAlert(
//                        requireContext(),
//                        getString(R.string.rate_gp),
//                        {
                            DataStore.setNotFirstOpen(true)
                            openMarket(requireContext(), requireActivity().packageName)
                          rlRateApp.inv()
//                        },
//                        {
//                          rlRateApp.inv()
//                        })
                }
            }
        }
        if (!DataStore.checkNotFirstOpen()){
//            rlRateApp.show()
//            showRate()
        }else{
//            if(AppUtil.isIAP){
//                adsView.visibility = View.GONE
//            }else{
//                adsView.show()
//                if(isNetworkConnected()){
//                    val layoutAds =
//                        LayoutInflater.from(context).inflate(R.layout.layout_ads_native_share, null)
//                    showAdsNative2("native_share", adsView, layoutAds)
//                }else{
//                    adsView.visibility = View.GONE
//                }
//            }

            rlRateApp.inv()
        }

    }


   private fun  showAds() {
       if (AppUtil.isIAP|| !isNetworkConnected()) {
       layoutAdsSaveAndShare?.gone()
       }else{
           lifecycle.addObserver(object : LifecycleEventObserver {
               override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                   if (event == Lifecycle.Event.ON_RESUME) {
                       Log.e("TAG", "onStateChanged: " + AppConstant.isShowingOpenAd)
                       layoutAdsSaveAndShare    ?.postDelayed({
                           if (!AppConstant.isShowingOpenAd) {
                               layoutAdsSaveAndShare      ?.show()
                           }
                       }, 1000)
                   } else if (event == Lifecycle.Event.ON_STOP) {
                       layoutAdsSaveAndShare ?.inv()
                   }
               }
           })
        AdsController.getInstance().loadAndShow(
            spaceName = "banner_save_and_share",
            layout = layoutAdsSaveAndShare,
            adCallback = object : AdCallback {
                override fun onAdClick() {
                    super.onAdClick()
                    AppConstant.checkInter = true
                }

                override fun onAdShow(network: String, adtype: String) {
                    viewGoneAdSaveAndShare?.gone()
                }

                override fun onAdClose(adType: String) {

                }

                override fun onAdFailToLoad(messageError: String?) {
                    layoutAdsSaveAndShare?.gone()
                }
                override fun onAdOff() {

                }

            })

    }
    }


    var numRate = 0
    private fun showRate() {
        isEnable(false)
        animScale(requireContext())
        animAlpha(requireContext())

        Handler(Looper.getMainLooper()).postDelayed({
            resetStar()
        }, 1700)

        Handler(Looper.getMainLooper()).postDelayed({
            resetStar()
            isEnable(true)
        }, 4200)
        setOnClickStar(requireContext())
    }

    private fun setOnClickStar(context: Context) {
        ivStar1.setPreventDoubleClickScaleView(300) {
            btnRate.showIfInv()
            btnRate.text = context.getString(R.string.feedback)
            numRate = 1
            view?.let { RateUtil.star1(it) }
            imThbest.visibility = View.VISIBLE
            img.visibility = View.VISIBLE
            tvContents.text = context.getString(R.string.pleaseleaveussomefeedback)
        }
        ivStar2.setPreventDoubleClickScaleView(300) {
            btnRate.showIfInv()
            numRate = 2
            btnRate.text = context.getString(R.string.feedback)
            view?.let { RateUtil.star2(it) }
            imThbest.visibility = View.VISIBLE
            img.visibility = View.VISIBLE
            tvContents.text = context.getString(R.string.pleaseleaveussomefeedback)

        }
        ivStar3.setPreventDoubleClickScaleView(300) {
            btnRate.showIfInv()
            numRate = 3
            btnRate.text = context.getString(R.string.feedback)
            view?.let { RateUtil.star3(it) }
            imThbest.visibility = View.VISIBLE
            img.visibility = View.VISIBLE
            tvContents.text = context.getString(R.string.pleaseleaveussomefeedback)
        }
        ivStar4.setPreventDoubleClickScaleView(300) {
            btnRate.showIfInv()
            numRate = 4
            btnRate.text = context.getString(R.string.feedback)
            view?.let { RateUtil.star4(it) }
            imThbest.visibility = View.VISIBLE
            img.visibility = View.VISIBLE
            tvContents.text = context.getString(R.string.welikeyoutoothanksforyourfeedback)
        }
        ivStar5.setPreventDoubleClickScaleView(300) {
            btnRate.showIfInv()
            numRate = 5
            btnRate.text = context.getString(R.string.rate_gp)
            view?.let { RateUtil.star5(it) }
            tvContents.text = context.getString(R.string.welikeyoutoothanksforyourfeedback)
        }


    }

    private fun loadImage() {
        path = AppUtil.savedImage
//        ImageUtil.setImage(ivSaveSmall, path)
//        ImageUtil.setImage(ivSaveLarge, path)
        Picasso.get().load(path).into(ivSaveSmall)
//        Picasso.get().load(path).into(ivSaveLarge)

    }


    private fun animScale(context: Context) {
        val animationScale1 =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale1)
        val animationScale2 =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale2)
        val animationScale3 =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale3)
        val animationScale4 =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale4)
        val animationScale5 =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale5)

        ivStar1?.startAnimation(animationScale1)
        ivStar2?.startAnimation(animationScale2)
        ivStar3?.startAnimation(animationScale3)
        ivStar4?.startAnimation(animationScale4)
        ivStar5?.startAnimation(animationScale5)

        ivStar1?.setImageResource(R.drawable.ic_star_up2)
        ivStar2?.setImageResource(R.drawable.ic_star_up2)
        ivStar3?.setImageResource(R.drawable.ic_star_up2)
        ivStar4?.setImageResource(R.drawable.ic_star_up2)
        ivStar5?.setImageResource(R.drawable.ic_star_up2)
    }

    private fun animAlpha(context: Context) {
        val animationAlpha1 =
            AnimationUtils.loadAnimation(context, R.anim.anim_alpha1)
        val animationAlpha2 =
            AnimationUtils.loadAnimation(context, R.anim.anim_alpha2)
        val animationAlpha3 =
            AnimationUtils.loadAnimation(context, R.anim.anim_alpha3)
        val animationAlpha4 =
            AnimationUtils.loadAnimation(context, R.anim.anim_alpha4)
        val animationAlpha5 =
            AnimationUtils.loadAnimation(context, R.anim.anim_alpha5)

        ivWave1?.visibility = View.VISIBLE
        ivWave2?.visibility = View.VISIBLE
        ivWave3?.visibility = View.VISIBLE
        ivWave4?.visibility = View.VISIBLE
        ivWave5?.visibility = View.VISIBLE

        ivWave1?.startAnimation(animationAlpha1)
        ivWave2?.startAnimation(animationAlpha2)
        ivWave3?.startAnimation(animationAlpha3)
        ivWave4?.startAnimation(animationAlpha4)
        ivWave5?.startAnimation(animationAlpha5)
    }

    private fun resetStar() {
        ivStar1?.setImageResource(R.drawable.ic_un_star_up)
        ivStar2?.setImageResource(R.drawable.ic_un_star_up)
        ivStar3?.setImageResource(R.drawable.ic_un_star_up)
        ivStar4?.setImageResource(R.drawable.ic_un_star_up)
        ivStar5?.setImageResource(R.drawable.ic_un_star_up)
    }

    private fun isEnable(isEnable: Boolean) {
        if (isEnable) {
            ivStar1?.isEnabled = true
            ivStar2?.isEnabled = true
            ivStar3?.isEnabled = true
            ivStar4?.isEnabled = true
            ivStar5?.isEnabled = true
        } else {
            ivStar1?.isEnabled = false
            ivStar2?.isEnabled = false
            ivStar3?.isEnabled = false
            ivStar4?.isEnabled = false
            ivStar5?.isEnabled = false
        }

    }

}