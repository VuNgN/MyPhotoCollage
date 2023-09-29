package com.sutech.photoeditor.view.gallery

import android.content.pm.PackageManager
import android.os.Handler
import android.widget.Toast
import com.sutech.photoeditor.R
import com.sutech.photoeditor.base.BaseFragment
import com.sutech.photoeditor.model.FolderGalleryObj
import com.sutech.photoeditor.model.ImageGalleryObj
import com.sutech.photoeditor.util.AppUtil
import com.sutech.photoeditor.util.DeviceDimensionsHelper.getDisplayWidth
import com.sutech.photoeditor.util.DialogUtil
import com.sutech.photoeditor.view.gallery.adapter.FolderGalleryAdapter
import com.sutech.photoeditor.view.gallery.adapter.ImageGalleryAdapter
import com.sutech.photoeditor.view.gallery.adapter.ImageSelectedAdapter
import kotlinx.android.synthetic.main.toolbar_gallery.*

class GalleryFrag : BaseFragment(R.layout.fragment_gallery) {

    val STORAGE_REQUEST = 100

    val listImage = mutableListOf<ImageGalleryObj>()
    val listImageSelected = mutableListOf<ImageGalleryObj>()
    val listFolder = mutableListOf<FolderGalleryObj>()
    var typeGetImage = -1L
    var maxImageSelected = 9
    var minImageSelected = 1
    var typeSelected: String? = AppUtil.EXTRA_TYPE_COLLAGE
    val adapterFolderGallery by lazy {
        FolderGalleryAdapter(listFolder) {
            if (it == 0) {
                typeGetImage = -1
                tvTitleGallery?.text = getString(R.string.all_photo)
                getAllImage()
            } else {
                tvTitleGallery?.text = listFolder[it].name
                typeGetImage = listFolder[it].id
                getAllImageByFolder(listFolder[it].id)
            }
            showAlbum(false)
        }
    }
    var adapterImageGallery: ImageGalleryAdapter? = null
    val adapterImageSelected by lazy {
        ImageSelectedAdapter((requireContext().getDisplayWidth() / 4.5).toInt(), listImageSelected) {
            onRemoveItemImage(listImageSelected[it])
        }
    }

    override fun onCreatedView() {
        context?.let {
            DialogUtil.showDialogLoading(it)
        }
        getDataBundle()
        initOnClick()
        initRcvFolderAdapter()
        initRcvImageAdapter()
        initRcvImageSelectedAdapter()
        showAds()
        Handler().postDelayed({
            getData()
        }, 200)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    if (listImage.isNullOrEmpty()) {
                        getFolder()
                        if (typeGetImage == -1L) {
                            getAllImage()
                        } else {
                            getAllImageByFolder(typeGetImage)
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
        }
    }
}