package com.sutech.photoeditor.view.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.sutech.photoeditor.R
import com.sutech.photoeditor.util.AppUtil.EXTRA_TYPE
import com.sutech.photoeditor.util.AppUtil.EXTRA_TYPE_COLLAGE
import com.sutech.photoeditor.util.AppUtil.EXTRA_TYPE_EDIT
import com.sutech.photoeditor.util.setPreventDoubleClickItemScaleView
import kotlinx.android.synthetic.main.fragment_home.btnCollageHome
import kotlinx.android.synthetic.main.fragment_home.btnEditHome
import kotlinx.android.synthetic.main.fragment_home.btnSavedHome
import kotlinx.android.synthetic.main.toolbar_home.btnSettingHome
import kotlinx.android.synthetic.main.toolbar_home.ivRemoveAds


fun HomeFrag.initOnClick() {

    logEvent("MainScr_Show")
    btnSettingHome?.setPreventDoubleClickItemScaleView {
        gotoFrag(R.id.homeFrag, R.id.action_homeFrag_to_settingFrag)
        logEvent("MainScr_IconSetting_Clicked")
    }


    btnCollageHome?.setPreventDoubleClickItemScaleView {
        logEvent("MainScr_ButtonCollage_Clicked")
        gotoCollage()
    }
    btnEditHome?.setPreventDoubleClickItemScaleView {
        logEvent("MainScr_ButtonEditPhoto_Clicked")
        gotoEdit()
    }
    btnSavedHome?.setPreventDoubleClickItemScaleView {
        logEvent("MainScr_ButtonSavePhoto_Clicked")
        gotoSaved()
    }

    ivRemoveAds.setOnClickListener {
        try {
            logEvent("MainScr_IconIAP_Clicked")
            findNavController().navigate(R.id.action_homeFrag_to_IAPFragment)
        } catch (e: Exception) {
        }
    }

}

fun HomeFrag.gotoCollage() {
    if (requestStoragePermissionItemCollage(100)) {
        val bundle = Bundle()
        bundle.putString(EXTRA_TYPE, EXTRA_TYPE_COLLAGE)
        gotoFrag(R.id.homeFrag, R.id.action_homeFrag_to_galleryFrag, bundle)
    }

}

fun HomeFrag.gotoEdit() {
    if (requestStoragePermissionItemCollage(200)) {
        val bundle = Bundle()
        bundle.putString(EXTRA_TYPE, EXTRA_TYPE_EDIT)
        gotoFrag(R.id.homeFrag, R.id.action_homeFrag_to_galleryFrag, bundle)
    }

}

fun HomeFrag.gotoSaved() {
    if (requestStoragePermissionItemCollage(300)) {
        gotoFrag(R.id.homeFrag, R.id.action_homeFrag_to_savedFrag)
    }
}

fun HomeFrag.requestStoragePermissionItemCollage(STORAGE_REQUEST: Int): Boolean {
    return if (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2
    ) {
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_REQUEST
        )
        false
    } else {
        true
    }
}