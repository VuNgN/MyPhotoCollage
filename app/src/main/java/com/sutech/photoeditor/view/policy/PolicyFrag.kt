package com.sutech.photoeditor.view.policy

import com.sutech.photoeditor.R
import com.sutech.photoeditor.base.BaseFragment
import com.sutech.photoeditor.util.setPreventDoubleClick
import kotlinx.android.synthetic.main.toolbar_base.*

class PolicyFrag : BaseFragment(R.layout.fragment_policy) {
    override fun onCreatedView() {
        btnBackToolbar.setPreventDoubleClick(1000){
            onBackPress()
        }
    }

}