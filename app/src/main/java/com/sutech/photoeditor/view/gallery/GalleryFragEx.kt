package com.sutech.photoeditor.view.gallery

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sutech.photoeditor.R
import com.sutech.photoeditor.model.ImageGalleryObj
import com.sutech.photoeditor.util.*
import com.sutech.photoeditor.util.AppUtil.EXTRA_TYPE_COLLAGE
import com.sutech.photoeditor.util.AppUtil.EXTRA_TYPE_EDIT
import com.sutech.photoeditor.view.gallery.adapter.FolderGalleryAdapter
import com.sutech.photoeditor.view.gallery.adapter.ImageGalleryAdapter
import com.sutech.ads.AdCallback
import com.sutech.ads.AdsController
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_gallery.*

fun GalleryFrag.initOnClick() {
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        if (rlAlbum.isShow()) {
            showAlbum(false)
        } else {
            onBackPress()
        }
    }

    logEvent("ImageScr_Show")


    btnCloseGallery?.setPreventDoubleClickScaleView(2000) {
        logEvent("ImageScr_IconBack_Clicked")
        onBackPress()
    }

    btnShowAlbum?.setPreventDoubleClickScaleView(500) {
        showAlbum(!rlAlbum.isShow())
    }

    btnDoneGallery?.setPreventDoubleClickScaleView(2000) {
        logEvent("ImageScr_IconNext_Clicked")
//        onClickDone(listImageSelected)
        if (listImageSelected.size >= minImageSelected) {
            when (typeSelected) {
                EXTRA_TYPE_COLLAGE -> {
                    AppUtil.listImageSelected = listImageSelected
                    gotoFrag(R.id.galleryFrag, R.id.action_galleryFrag_to_puzzleFrag)
                }
                EXTRA_TYPE_EDIT -> {
                    AppUtil.imageSelected = listImageSelected[0]
                    gotoFrag(R.id.galleryFrag, R.id.action_galleryFrag_to_editFrag)
                }
            }
        } else {
            AppUtil.showToast(context, getString(R.string.min_image_selected) + minImageSelected)
        }
    }
//    btnAllPhoto?.setPreventDoubleClickScaleView(2000) {
//        showAlbum(true)
//    }
//    btnCloseAlbum?.setPreventDoubleClickScaleView(2000) {
//        showAlbum(false)
//    }
    iconSelected?.setPreventDoubleClickScaleView(200) {
        showImageSelected(!rcvImageSelected.isShow())
    }
    btnSelected?.setPreventDoubleClickScaleView(200) {
        showImageSelected(!rcvImageSelected.isShow())
    }
    btnDeselectedAll?.setPreventDoubleClickScaleView(2000) {
        listImageSelected.clear()
        adapterImageGallery?.hashMap?.clear()
        adapterImageGallery?.notifyDataSetChanged()
        adapterImageSelected.notifyDataSetChanged()
        setTotalImageSelected()
    }
    setTotalImageSelected()
}

fun GalleryFrag.initRcvFolderAdapter() {
    rcvAlbum.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    rcvAlbum.adapter = adapterFolderGallery
}

fun GalleryFrag.initRcvImageAdapter() {
    if (adapterImageGallery == null) {
        adapterImageGallery = ImageGalleryAdapter(typeSelected == EXTRA_TYPE_EDIT, listImage, {
//        adapterImageGallery = ImageGalleryAdapter(false, listImage, {
            if (it<listImage.size){
                onAddItemImage(it, listImage[it])
            }
        }, { if (it<listImage.size){
            onRemoveItemImage(listImage[it])

        }
        }, { if (it<listImage.size){
            listImageSelected.clear()
            adapterImageSelected.notifyDataSetChanged()
            if (it != -1) {
                adapterImageGallery?.notifyItemChanged(it)
            }

        }
            setTotalImageSelected()
        })
    }

    context?.let {
            ctx->
        rcvImage.layoutManager = GridLayoutManager(ctx, 3, RecyclerView.VERTICAL, false)
        rcvImage.adapter = adapterImageGallery
    }


}

fun GalleryFrag.initRcvImageSelectedAdapter() {
    rcvImageSelected.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    rcvImageSelected.adapter = adapterImageSelected

}

fun GalleryFrag.getData() {
    if (listImage.isNullOrEmpty()) {

        getAllImage()
        getFolder()
    }else{DialogUtil.cancelDialogLoading()}
}

fun GalleryFrag.requestStoragePermission(): Boolean {
    return if (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
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

fun GalleryFrag.getDataBundle() {
    arguments?.let {
        typeSelected = it.getString(AppUtil.EXTRA_TYPE)
        when (typeSelected) {
            EXTRA_TYPE_COLLAGE -> {
//                tvTitleGallery?.text = getString(R.string.choose_images)
                maxImageSelected = 9
                minImageSelected = 2
            }
            EXTRA_TYPE_EDIT -> {
                rlImageSelected?.layoutParams?.height = 0
//                tvTitleGallery?.text = getString(R.string.choose_image)
                maxImageSelected = 1
                minImageSelected = 1
            }
        }
    }
}


fun GalleryFrag.showAlbum(isShow: Boolean) {
    if (isShow) {
        ImageUtil.setImage(icMoreAlbum,R.drawable.ic_more_up)
        slideDownAlbum()
    } else {
        ImageUtil.setImage(icMoreAlbum,R.drawable.ic_more_down)
        slideUpAlbum()
    }
}

fun GalleryFrag.slideUpAlbum() {
//    rlAlbum?.show()
//    val animate = TranslateAnimation(
//        0F,  // fromXDelta
//        0F,  // toXDelta
//        0F,  // fromYDelta
//        -rlAlbum?.height!!.toFloat()
//    ) // toYDelta
//    animate.duration = 200
//    animate.fillAfter = true
//    animate.setAnimationListener(object : Animation.AnimationListener {
//        override fun onAnimationStart(animation: Animation) {}
//        override fun onAnimationEnd(animation: Animation) {
//            rlAlbum?.clearAnimation()
            rlAlbum?.gone()
//        }
//
//        override fun onAnimationRepeat(animation: Animation) {}
//    })
//    rlAlbum?.startAnimation(animate)

//    rlAlbum?.show()
//    val animate = TranslateAnimation(
//        0F,  // fromXDelta
//        0F,  // toXDelta
//        rlAlbum?.height!!.toFloat(),  // fromYDelta
//        0F
//    ) // toYDelta
//    animate.duration = 200
//    animate.fillAfter = true
//    rlAlbum?.startAnimation(animate)
}

fun GalleryFrag.slideDownAlbum() {
    rlAlbum?.show()
//    val animate = TranslateAnimation(
//        0F,  // fromXDelta
//        0F,  // toXDelta
//        -rlAlbum?.height!!.toFloat(),  // fromYDelta
//        0f
//    ) // toYDelta
//    animate.duration = 200
//    animate.fillAfter = true

//    rlAlbum?.startAnimation(animate)
//    val animate = TranslateAnimation(
//        0F,  // fromXDelta
//        0F,  // toXDelta
//        0F,  // fromYDelta
//        rlAlbum?.height!!.toFloat()
//    ) // toYDelta
//    animate.duration = 200
//    animate.fillAfter = true
//    animate.setAnimationListener(object : Animation.AnimationListener {
//        override fun onAnimationStart(animation: Animation) {}
//        override fun onAnimationEnd(animation: Animation) {
//            rlAlbum?.clearAnimation()
//            rlAlbum?.gone()
//        }
//
//        override fun onAnimationRepeat(animation: Animation) {}
//    })
//    rlAlbum?.startAnimation(animate)

}

fun GalleryFrag.showImageSelected(isShow: Boolean) {
    if (isShow) {
        slideUpImageSelected()
        ImageUtil.setImage(iconSelected, R.drawable.ic_hide)
        btnDeselectedAll.show()
        btnAllPhoto.gone()
        iconAlbumShow.gone()
    } else {
        slideDownImageSelected()
        ImageUtil.setImage(iconSelected, R.drawable.ic_show)
        btnDeselectedAll.gone()
        iconAlbumShow.show()
        btnAllPhoto.show()
    }
}

fun GalleryFrag.slideUpImageSelected() {
    rcvImageSelected?.show()
    val animate = TranslateAnimation(
        0F,  // fromXDelta
        0F,  // toXDelta
        rcvImageSelected?.height!!.toFloat(),  // fromYDelta
        0F
    ) // toYDelta
    animate.duration = 200
    animate.fillAfter = true
    rcvImageSelected?.startAnimation(animate)
}

fun GalleryFrag.slideDownImageSelected() {
    val animate = TranslateAnimation(
        0F,  // fromXDelta
        0F,  // toXDelta
        0F,  // fromYDelta
        rcvImageSelected?.height!!.toFloat()
    ) // toYDelta
    animate.duration = 200
    animate.fillAfter = true
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
            rcvImageSelected.gone()
        }

        override fun onAnimationRepeat(animation: Animation) {}
    })
    rcvImageSelected?.startAnimation(animate)
}

fun GalleryFrag.onAddItemImage(
    position: Int,
    image: ImageGalleryObj
) {
    if (listImageSelected.size < maxImageSelected) {
        image.countSelected = image.countSelected + 1
        listImageSelected.add(image)
        adapterImageSelected.notifyItemInserted(position)
        adapterImageGallery!!.hashMap[image.id] = image
        adapterImageGallery!!.notifyItemChanged(position, ImageGalleryAdapter.InfoMessageChanged())
        setTotalImageSelected()
    } else {
        AppUtil.showToast(context, getString(R.string.max_image_selected) + maxImageSelected)
//            context?.showDialogMaxImage(10)
    }

}

@SuppressLint("SetTextI18n")
fun GalleryFrag.setTotalImageSelected() {
    tvSizeSelected?.text = "${listImageSelected.size}/$maxImageSelected ${getString(R.string.selection_photo)}"
        if (listImageSelected.isNullOrEmpty()){
            tvSizeSelected?.inv()
            tvLoadingSelected?.show()
        }else{
            tvSizeSelected?.show()
            tvLoadingSelected?.gone()
        }
//        if (listImageSelected.size>=minImageSelected){
        if (listImageSelected.size>=1){
            btnDoneGallery?.show()
        }else{
            btnDoneGallery?.gone()
        }
}

fun GalleryFrag.showAds() {
    if (AppUtil.isIAP|| !isNetworkConnected()) {
        layoutAdsGallery.gone()
    }else{
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    Log.e("TAG", "onStateChanged: " + AppConstant.isShowingOpenAd)
                    layoutAdsGallery   ?.postDelayed({
                        if (!AppConstant.isShowingOpenAd) {
                            layoutAdsGallery  ?.show()
                        }
                    }, 1000)
                } else if (event == Lifecycle.Event.ON_STOP) {
                    layoutAdsGallery  ?.inv()
                }
            }
        })
        AdsController.getInstance().loadAndShow(
            spaceName = "banner_gallery",
            layout = layoutAdsGallery,
            adCallback = object : AdCallback {
                override fun onAdClick() {
                    super.onAdClick()
                    AppConstant.checkInter = true
                }

                override fun onAdShow(network: String, adtype: String) {
                    viewGoneAdGallery?.gone()
                }

                override fun onAdClose(adType: String) {

                }

                override fun onAdFailToLoad(messageError: String?) {
                    layoutAdsGallery?.gone()
                }

                override fun onAdOff() {

                }

            })
    }
}
fun GalleryFrag.getFolder() {
    if (requestStoragePermission()) {

        context?.let {
                ctx->
            val folders = DeviceUtil.getFolderImage(ctx)
            DialogUtil.cancelDialogLoading()
            if (!folders.isNullOrEmpty()) {
                listFolder.clear()
                listFolder.addAll(folders)

                if (listFolder.size > 0 ){
                    tvTitleGallery?.text = listFolder[0].name
                }
                if (listFolder.size > 0 && listImage.size > 0) {
                    if (listFolder[0].path == null) {
                        listFolder[0].path = listImage[0].pathImage
                    }
                }
                adapterFolderGallery.notifyDataSetChanged()

            }
        }

    }
}

fun GalleryFrag.getAllImage() {
    if (requestStoragePermission()) {

        context?.let {
                ctx->

            val images = DeviceUtil.getAllImage(ctx)
            if (!images.isNullOrEmpty()) {
                listImage.clear()
                listImage.addAll(images)
                if (listFolder.size > 0) {
                    if (listFolder[0].path == null) {
                        listFolder[0].path = images[0].pathImage
                        adapterFolderGallery.notifyItemChanged(
                            0,
                            FolderGalleryAdapter.InfoMessageChanged()
                        )
                    }
                }
                adapterImageGallery?.notifyDataSetChanged()
            }
        }

    }
}

fun GalleryFrag.getAllImageByFolder(idFolder: Long) {
    if (requestStoragePermission()) {

        context?.let {
                ctx->
            val images = DeviceUtil.getImageFromFolder(ctx, idFolder)
            listImage.clear()
            if (!images.isNullOrEmpty()) {
                listImage.addAll(images)
            }
            adapterImageGallery?.notifyDataSetChanged()

        }
         }
}

fun GalleryFrag.onRemoveItemImage(
    image: ImageGalleryObj
) {
    var imageRemove = image
    for (i in 0 until listImageSelected.size) {
        if (listImageSelected[i].id == image.id) {
            imageRemove = listImageSelected[i]
            break
        }
    }

    listImageSelected.remove(imageRemove)
    image.countSelected = image.countSelected - 1
    adapterImageGallery!!.hashMap[image.id] = image

    adapterImageSelected.notifyDataSetChanged()
    adapterImageGallery!!.notifyItemChanged(
        image.positionInAdapter,
        ImageGalleryAdapter.InfoMessageChanged()
    )
    setTotalImageSelected()


}
