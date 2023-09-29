package com.sutech.photoeditor.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView


@SuppressLint("AppCompatCustomView","CustomViewStyleable")
class IconView : ImageView {
    constructor(context: Context?) : super(context) {
//        this.setColorFilter(ColorUtil.instant!!.colorIcon)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
//        this.setColorFilter(ColorUtil.instant!!.colorIcon)
    }

    constructor(
        context: Context?, attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
//        this.setColorFilter(ColorUtil.instant!!.colorIcon)
    }
}