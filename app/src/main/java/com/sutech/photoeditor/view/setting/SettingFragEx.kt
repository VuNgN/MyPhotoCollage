package com.sutech.photoeditor.view.setting

import SettingFrag
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.sutech.photoeditor.BuildConfig
import com.sutech.photoeditor.R
import com.sutech.photoeditor.util.AppConstant.FOLDER_PHOTO_LAYOUT
import com.sutech.photoeditor.util.AppUtil.openBrowser
import com.sutech.photoeditor.util.AppUtil.sendEmailFeedBack
import com.sutech.photoeditor.util.setPreventDoubleClickScaleView
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.toolbar_base.*


fun SettingFrag.initOnClick() {
    logEvent("SettingScr_Show")
    btnBackToolbar?.setPreventDoubleClickScaleView {
        logEvent("SettingScr_IconBack_Clicked")
        onBackPress()
    }

//    btnRate?.setPreventDoubleClickScaleView(1000) {
//        openMarket(requireContext(), requireActivity().packageName)
//    }
    btnFeedback?.setPreventDoubleClickScaleView(1000) {
        logEvent("SettingScr_Feedback_Clicked")
        sendEmailFeedBack(
            requireContext(),
            arrayOf("sutechmobile@gmail.com"),
            "Feedback to ${getString(R.string.app_name)}",
            ""
        )
    }
    btnRateApp?.setPreventDoubleClickScaleView(1000) {
        logEvent("SettingScr_IconRateApp_Clicked")
        val url =  "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID.trimIndent()}"
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
    }
    btnCheckUpdate?.setPreventDoubleClickScaleView(1000) {
        logEvent("SettingScr_Checkforupdate_Clicked")
        val url =  "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID.trimIndent()}"
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
    }
    btnPolicy?.setPreventDoubleClickScaleView(1000) {
        logEvent("SettingScr_IconPolicy_Clicked")
        openBrowser(requireContext(),"https://sutechmobile.blogspot.com/2021/09/photo-collage-maker-privacy-policy.html")
//        gotoFrag(R.id.settingFrag,R.id.action_settingFrag_to_policyFrag)
    }
    btnShare?.setPreventDoubleClickScaleView(1000) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, FOLDER_PHOTO_LAYOUT)
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage =
                "${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}".trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: java.lang.Exception) {

        }
    }

}



