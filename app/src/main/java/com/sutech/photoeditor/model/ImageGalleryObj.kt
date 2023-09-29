package com.sutech.photoeditor.model

import android.graphics.Bitmap
import android.net.Uri

data class ImageGalleryObj(
    val id: Long,
    var pathImage: Uri,
    var countSelected: Int=0,
    var positionInAdapter: Int = 0,
    var bitmap: Bitmap? = null
)