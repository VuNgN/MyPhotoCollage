package com.sutech.photoeditor.model

import android.net.Uri

data class FolderGalleryObj(
    val id: Long=0,
    val name: String="",
    var path: Uri?=null,
    var size: Int =0
)
