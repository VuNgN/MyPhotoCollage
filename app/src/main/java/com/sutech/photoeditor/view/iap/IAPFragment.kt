package com.sutech.photoeditor.view.iap

import android.content.Intent
import android.graphics.Paint
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.*
import com.sutech.photoeditor.R
import com.sutech.photoeditor.base.BaseFragment
import com.sutech.photoeditor.util.AppUtil
import com.sutech.photoeditor.view.splash.IapCommon
import kotlinx.android.synthetic.main.fragment_i_a_p.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class   IAPFragment : BaseFragment(R.layout.fragment_i_a_p)  {
    override fun onCreatedView() {
        tvOldPrice.paintFlags = tvOldPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
        logEvent("IAPScr_Show")
        tvOldPrice.setText(AppUtil.PRICE_FOREVER_FAKE)
        tvPrice.setText(AppUtil.PRICE_FOREVER)
        tvBuyNow?.setOnClickListener {
            logEvent("IAPScr_ButtonBuyNow_Clicked")
            IapCommon.buyIap(AppUtil.SKU_FOREVER)
        }

        ivBack?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}