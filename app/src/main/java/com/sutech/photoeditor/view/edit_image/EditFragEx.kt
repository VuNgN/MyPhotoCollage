package com.sutech.photoeditor.view.edit_image

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.os.Handler
import android.os.Looper
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.internal.view.SupportMenu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.sutech.photoeditor.R
import com.sutech.photoeditor.editor.featuresfoto.adjust.AdjustAdapter
import com.sutech.photoeditor.editor.featuresfoto.splash.SplashDialog
import com.sutech.photoeditor.editor.featuresfoto.sticker.adapter.StickerAdapter
import com.sutech.photoeditor.editor.featuresfoto.sticker.adapter.TopTabEditAdapter
import com.sutech.photoeditor.editor.filterscolor.DegreeSeekBar
import com.sutech.photoeditor.editor.filterscolor.FilterUtils
import com.sutech.photoeditor.editor.filterscolor.FilterViewAdapter
import com.sutech.photoeditor.editor.sticker.BitmapStickerIcon
import com.sutech.photoeditor.editor.sticker.Sticker
import com.sutech.photoeditor.editor.sticker.StickerView
import com.sutech.photoeditor.editor.sticker.TextSticker
import com.sutech.photoeditor.editor.sticker.event.AlignHorizontallyEvent
import com.sutech.photoeditor.editor.sticker.event.DeleteIconEvent
import com.sutech.photoeditor.editor.sticker.event.FlipHorizontallyEvent
import com.sutech.photoeditor.editor.sticker.event.ZoomIconEvent
import com.sutech.photoeditor.model.MenuObj
import com.sutech.photoeditor.model.MenuType
import com.sutech.photoeditor.util.*
import com.sutech.photoeditor.widget.customview.photo.OnSaveBitmap
import com.isseiaoki.simplecropview.CropImageView
import com.sutech.photoeditor.editor.featuresfoto.adjust.AdjustListener
import com.sutech.ads.AdCallback
import com.sutech.ads.AdsController
import kotlinx.android.synthetic.main.fragment_edit_image.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.layout_adjust.*
import kotlinx.android.synthetic.main.layout_crop.*
import kotlinx.android.synthetic.main.layout_filter.*
import kotlinx.android.synthetic.main.layout_over_play.*
import kotlinx.android.synthetic.main.layout_sticker.*
import kotlinx.android.synthetic.main.toolbar_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs


fun EditFrag.showAds() {
//    AdsController.getInstance().loadAndShow(
//        spaceName = "banner_edit_image",
//        layout = layoutAdsEditImage,
//        adCallback = object : AdCallback {
//            override fun onAdClick() {
//                super.onAdClick()
//                AppConstant.checkInter = true
//            }
//
//            override fun onAdShow(network: String, adtype: String) {
                viewGoneAdEditImage?.gone()
//            }
//
//            override fun onAdClose(adType: String) {
//
//            }
//
//            override fun onAdFailToLoad(messageError: String?) {
//                layoutAdsEditImage?.gone()
//            }
//
//            override fun onAdOff() {
//
//            }
//
//        })

}

fun EditFrag.initOnClick() {
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        if (canBack) {
            onClickBack()
        }
    }

    Handler(Looper.myLooper()!!).postDelayed({
        canBack = true
        btnBackEdit?.setPreventDoubleClickItemScaleView {
            logEvent("EditPhotoScr_IconBack_Clicked")
            onBackPress()
        }
        btnCloseFeature?.setPreventDoubleClickItemScaleView {
            onClickBack()
        }
    }, 1000)

    logEvent("EditPhotoScr_Show")

    btnDoneEdit?.setPreventDoubleClickItemScaleView(2000) {
        logEvent("EditPhotoScr_IconSave_Clicked")
        try {
            photoEditorView?.setHandlingSticker(null)
            photoEditorView?.glSurfaceView?.alpha = 0.0f

            context?.let {
            c->
                ViewToBitmap.of(photoEditorView).toJPG()
                    .setOnSaveResultListener { isSaved, path, uri ->
                        DialogUtil.cancelDialogLoading()
                        if (uri != null) {
                            photoEditorView?.glSurfaceView?.alpha = 1.0f
                            AppUtil.savedImage = uri

                            AppUtil.countAdsEdit ++
                            if (AppUtil.isIAP||AppUtil.countAdsEdit%2==0|| !isNetworkConnected()) {
                                gotoFrag(
                                    R.id.editFrag,
                                    R.id.action_editFrag_to_saveAndShareFrag
                                )
                            }else{
                                showAdInter("inter_edit_image", 8000, getString(R.string.loading_ad_2), {
                                    gotoFrag(
                                        R.id.editFrag,
                                        R.id.action_editFrag_to_saveAndShareFrag
                                    )

                                }, {

                                }, {
                                    gotoFrag(
                                        R.id.editFrag,
                                        R.id.action_editFrag_to_saveAndShareFrag
                                    )
                                }, {

                                })
                            }


                        }
                    }.save(c)
            }
        }catch (e:Exception){
        }
    }

    btnSaveEdit?.setPreventDoubleClickItemScaleView {

        context?.let {
            DialogUtil.showDialogLoading(it)
        }

        when (menuType) {
            MenuType.EDIT_MAIN -> {
            }

            MenuType.EDIT_FILTER,
            MenuType.EDIT_ADJUST,
            MenuType.EDIT_OVERPLAY
            -> {

                photoEditorView.saveGLSurfaceViewAsBitmap(object : OnSaveBitmap {
                    override fun onFailure(exc: java.lang.Exception?) {
                        DialogUtil.cancelDialogLoading()
                    }

                    override fun onBitmapReady(bitmap: Bitmap?) {
                        DialogUtil.cancelDialogLoading()
                        updateBitmap(bitmap)
                        photoEditorView.setFilterEffect("")
                    }
                })
                menuType = MenuType.EDIT_MAIN
                onClickMenuMain()
            }
            MenuType.EDIT_STICKER,
            MenuType.EDIT_TEXT -> {

                photoEditorView.setHandlingSticker(null)
                photoEditorView.glSurfaceView.alpha = 0.0f
                photoEditor.saveStickerAsBitmap(object : OnSaveBitmap {
                    override fun onFailure(exc: java.lang.Exception?) {
                        DialogUtil.cancelDialogLoading()
                        menuType = MenuType.EDIT_MAIN
                        onClickMenuMain()
                    }

                    override fun onBitmapReady(bitmap: Bitmap?) {
                        try {
                            DialogUtil.cancelDialogLoading()
                            photoEditorView.glSurfaceView.alpha = 1.0f
                            photoEditorView.stickers.clear()
                            updateBitmap(bitmap)
                            menuType = MenuType.EDIT_MAIN
                            onClickMenuMain()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                })
            }
            MenuType.EDIT_SQUARE,
            MenuType.EDIT_SPLASH,
            MenuType.EDIT_BLUR -> {
                DialogUtil.cancelDialogLoading()
                menuType = MenuType.EDIT_MAIN
                onClickMenuMain()
            }
            MenuType.EDIT_CROP -> {
                DialogUtil.cancelDialogLoading()
                updateBitmap(cropImageView.croppedBitmap)
                menuType = MenuType.EDIT_MAIN
                onClickMenuMain()
            }
            else -> {
                DialogUtil.cancelDialogLoading()
            }
        }
    }
}

fun EditFrag.onClickBack() {
    when (menuType) {
        MenuType.EDIT_MAIN,
        MenuType.EDIT_SPLASH,
        MenuType.EDIT_BLUR -> {
//            if(AppUtil.isIAP){
                onBackPress()
//            }else{
//                if (mInterstitialAd.isLoaded) {
//                    mInterstitialAd.show()
//                } else {
//                    onBackPress()
//                }
//                mInterstitialAd.adListener = object : AdListener() {
//                    override fun onAdLoaded() {
//                        // dialog.dismiss()
//
//                    }
//
//                    override fun onAdFailedToLoad(errorCode: Int) {
//                        onBackPress()
//
//                    }
//
//                    override fun onAdOpened() {
//                        onBackPress()
//                    }
//
//                    override fun onAdClicked() {
//                        // Code to be executed when the user clicks on an ad.
//                    }
//
//                    override fun onAdLeftApplication() {
//                        // Code to be executed when the user has left the app.
//                    }
//
//                    override fun onAdClosed() {
//
//                        // Code to be executed when the interstitial ad is closed.
//                    }
//                }
//            }


        }
        MenuType.EDIT_ADJUST,
        MenuType.EDIT_FILTER,
        MenuType.EDIT_OVERPLAY -> {
            photoEditorView.setFilterEffect("")
            menuType = MenuType.EDIT_MAIN
            filterViewAdapter?.selectedFilterIndex =0
            overplayViewAdapter?.selectedFilterIndex =0
            adjustAdapter?.selectedFilterIndex =0

            try {
                val adjust = adjustAdapter!!.lstAdjusts[0]
                adjust?.let {
                    sbAdjust.setCurrentDegrees(((it.originValue - it.minValue) / ((it.maxValue - ((it.maxValue + it.minValue) / 2.0f)) / 50.0f) - 50).toInt())
                }
            }catch (e:Exception){}
            filterViewAdapter?.notifyDataSetChanged()
            overplayViewAdapter?.notifyDataSetChanged()
            adjustAdapter?.notifyDataSetChanged()
            onClickMenuMain()
        }
        MenuType.EDIT_STICKER,
        MenuType.EDIT_TEXT,
        MenuType.EDIT_CROP,
        MenuType.EDIT_SQUARE -> {

            photoEditorView?.setHandlingSticker(null)
            photoEditorView?.stickers?.clear()
            menuType = MenuType.EDIT_MAIN
            onClickMenuMain()
        }
        else -> {
        }
    }
}

fun EditFrag.onClickMenuMain() {
    hideAllMenu()
    menuEdit.layoutParams.height = context?.convertDpToPx(50)!!
//    ImageUtil.setImage(btnSaveEdit, R.drawable.ic_tick, 1f)
    when (menuType) {
        MenuType.EDIT_MAIN -> {
            menuEdit.layoutParams.height = 0
//            ImageUtil.setImage(btnBackEdit, R.drawable.ic_close, 1f)
//            ImageUtil.setImage(btnSaveEdit, R.drawable.ic_saved_home, 1f)
//            tvTitleMenuEdit?.text = getString(R.string.edit_image)
            rcvMenuEditMain?.slideShowToFade(durationAnimMenu)
        }
        MenuType.EDIT_FILTER -> {
            logEvent("EditPhotoScr_IconFilter_Clicked")
            ImageUtil.setImage(btnBackEdit, R.drawable.ic_close, 1f)
            layoutFilter?.slideShowToFade(durationAnimMenu)
            tvTitleMenuEdit?.text = getString(R.string.filter)
        }
        MenuType.EDIT_STICKER -> {

            logEvent("EditPhotoScr_IconSticker_Clicked")
            ImageUtil.setImage(btnBackEdit, R.drawable.ic_close, 1f)
            layoutSticker?.slideShowToFade(durationAnimMenu)
            tvTitleMenuEdit?.text = getString(R.string.sticker)
        }
        MenuType.EDIT_TEXT -> {
        }
        MenuType.EDIT_CROP -> {
            logEvent("EditPhotoScr_IconCut_Clicked")
            ImageUtil.setImage(btnBackEdit, R.drawable.ic_close, 1f)
            layoutCropImage?.show()
            cropImageView?.imageBitmap = photoEditorView?.currentBitmap
            tvTitleMenuEdit?.text = getString(R.string.crop)
        }
        MenuType.EDIT_ADJUST -> {
            logEvent("EditPhotoScr_IconAdjust_Clicked")
            ImageUtil.setImage(btnBackEdit, R.drawable.ic_close, 1f)
            layoutAdjust?.slideShowToFade(durationAnimMenu)
            tvTitleMenuEdit?.text = getString(R.string.adjust)
            initAdjust()
        }
        MenuType.EDIT_OVERPLAY -> {
            ImageUtil.setImage(btnBackEdit, R.drawable.ic_close, 1f)
            layoutOverplay?.slideShowToFade(durationAnimMenu)
            tvTitleMenuEdit?.text = getString(R.string.overplay)
        }
        MenuType.EDIT_SQUARE -> {
            ImageUtil.setImage(btnBackEdit, R.drawable.ic_close, 1f)
            tvTitleMenuEdit?.text = getString(R.string.square)
        }

        MenuType.EDIT_SPLASH -> {

            CoroutineScope(Dispatchers.IO).launch {
                currentBitmap = photoEditorView.currentBitmap
                val greyBitMap = FilterUtils.getBlackAndWhiteImageFromBitmap(currentBitmap)
                withContext(Dispatchers.Main) {
                    SplashDialog.show(
                        requireActivity().supportFragmentManager,
                        currentBitmap,
                        null,
                        greyBitMap,
                        { bitmap ->
                            updateBitmap(bitmap)
                        },
                        true
                    )
                    menuType = MenuType.EDIT_MAIN
                    onClickMenuMain()
                }

            }


        }
        MenuType.EDIT_BLUR -> {
            CoroutineScope(Dispatchers.IO).launch {
                currentBitmap = photoEditorView.currentBitmap

                val blurBitmap = FilterUtils.getBlurImageFromBitmap(currentBitmap)
                withContext(Dispatchers.Main) {
                    SplashDialog.show(
                        requireActivity().supportFragmentManager,
                        currentBitmap,
                        blurBitmap,
                        null,
                        { bitmap ->
                            updateBitmap(bitmap)
                        },
                        false
                    )
                    menuType = MenuType.EDIT_MAIN
                    onClickMenuMain()
                }

            }
        }

        else -> {
        }
    }
//        MenuType. EDIT_BLUR-> { }
}

fun EditFrag.updateBitmap(bitmap: Bitmap?) {
    currentBitmap = bitmap
    photoEditorView?.setImageSource(bitmap)
    photoEditorView?.invalidate()
    updateLayout()
}

fun EditFrag.hideAllMenu() {
    rcvMenuEditMain?.gone()
    layoutSticker?.gone()
    layoutFilter?.gone()
    layoutAdjust?.gone()
    layoutOverplay?.gone()
    layoutCropImage?.gone()
}

fun EditFrag.setBackgroundImage() {
    val width = AppUtil.getWidthScreen(requireActivity())

    imageObj = AppUtil.imageSelected
    if (imageObj != null) {

//        activity?.let {
//                ctx->
            Glide.with(requireActivity()).asBitmap().load(imageObj!!.pathImage)
                .priority(Priority.HIGH).apply(
                    RequestOptions().override(width, width)
                ).addListener(object :
                    RequestListener<Bitmap?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        context?.let {
                                ctx->

                            if (isAdded && isVisible && isResumed && !AppUtil.isNetworkAvailable(
                                    ctx
                                )
                            ) {
                                AppUtil.showToast(
                                    context,
                                    "Please enable Internet connection to be able to load images"
                                )
                            }

                        }
                        return false
                    }
                }).into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        photoEditorView.isConstrained = true
                        updateBitmap(resource)
                        initFilter()
                        initOverplay()
                    }

                })

        }

//    }

}

fun EditFrag.updateLayout() {
    photoEditorView.postDelayed({
        try {
            val defaultDisplay: Display = requireActivity().windowManager.defaultDisplay
            val point = Point()
            defaultDisplay.getSize(point)
            val i = point.x
            val height: Int = wrapPhotoView.height
            val i2: Int = photoEditorView.glSurfaceView
                .renderViewport.width
            val f = photoEditorView.glSurfaceView.renderViewport.height.toFloat()
            val f2 = i2.toFloat()
            if ((i.toFloat() * f / f2).toInt() <= height) {
                val layoutParams =
                    RelativeLayout.LayoutParams(-1, -2)
                layoutParams.addRule(13)
                photoEditorView?.layoutParams = layoutParams
                photoEditorView?.show()
            } else {
                val layoutParams2 = RelativeLayout.LayoutParams(
                    (height.toFloat() * f2 / f).toInt(),
                    -1
                )
                layoutParams2.addRule(13)
                photoEditorView?.layoutParams = layoutParams2
                photoEditorView?.show()
            }
        } catch (unused: Exception) {
        }

    }, 300)
}

fun EditFrag.initMenuEditMain() {
    if (listMenuEditMain.isEmpty()) {
        addMenuEditMain()
    }
    rcvMenuEditMain?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    rcvMenuEditMain?.adapter = adapterMenuEditMain
}

fun EditFrag.addMenuEditMain() {
    listMenuEditMain.clear()
    listMenuEditMain.add(
        MenuObj(
            MenuType.EDIT_FILTER,
            getString(R.string.filter),
            R.drawable.ic_filter
        )
    )
    listMenuEditMain.add(
        MenuObj(
            MenuType.EDIT_STICKER,
            getString(R.string.sticker),
            R.drawable.ic_sticker
        )
    )
//    listMenuEditMain.add(MenuObj(MenuType.EDIT_TEXT, getString(R.string.text), R.drawable.ic_text))
    listMenuEditMain.add(MenuObj(MenuType.EDIT_CROP, getString(R.string.crop), R.drawable.ic_crop))
    listMenuEditMain.add(
        MenuObj(
            MenuType.EDIT_ADJUST,
            getString(R.string.adjust),
            R.drawable.ic_adjust
        )
    )
    listMenuEditMain.add(
        MenuObj(
            MenuType.EDIT_OVERPLAY,
            getString(R.string.overplay),
            R.drawable.ic_overlay
        )
    )
//    listMenuEditMain.add(MenuObj(MenuType.EDIT_SQUARE,getString(R.string.square),R.drawable.crop_free_click))
    listMenuEditMain.add(
        MenuObj(
            MenuType.EDIT_SPLASH,
            getString(R.string.splash),
            R.drawable.ic_splash
        )
    )
    listMenuEditMain.add(
        MenuObj(
            MenuType.EDIT_BLUR,
            getString(R.string.blur),
            R.drawable.ic_blur_two
        )
    )
}

fun EditFrag.initFilter() {
    sbFilter?.setOnSeekBarChangeListener(this)
    listBitmapFilter.clear()
    CoroutineScope(Dispatchers.IO).launch {
        val listFilter = FilterUtils.getLstBitmapWithFilter(
            ThumbnailUtils.extractThumbnail(
                currentBitmap,
                150,
                150
            )
        )

        withContext(Dispatchers.Main) {
            if (!listFilter.isNullOrEmpty()) {
                listBitmapFilter.addAll(listFilter)
            }
            context?.let {
                filterViewAdapter = FilterViewAdapter(
                    listBitmapFilter,
                    this@initFilter,
                    it,
                    (FilterUtils.EFFECT_CONFIGS).toList()
                )

                rcvEditFilter?.adapter = filterViewAdapter
            }
        }
    }
}

fun EditFrag.initOverplay() {
    sbOverplay?.setOnSeekBarChangeListener(this)
    listBitmapOverplay.clear()
    CoroutineScope(Dispatchers.IO).launch {
        val listOverplay = FilterUtils.getLstBitmapWithOverlay(
            ThumbnailUtils.extractThumbnail(
                currentBitmap,
                150,
                150
            )
        )

        withContext(Dispatchers.Main) {
            if (!listOverplay.isNullOrEmpty()) {
                listBitmapOverplay.addAll(listOverplay)
            }
            context?.let {
            overplayViewAdapter =FilterViewAdapter(
                listBitmapOverplay,
                this@initOverplay,
                it,
                (FilterUtils.OVERLAY_CONFIG).toList()
            )
                rcvEditOverplay?.adapter = overplayViewAdapter
            }
        }
    }
}

fun EditFrag.initAdjust() {

    context?.let {
            ctx->

        adjustAdapter = AdjustAdapter(ctx, AdjustListener {
            sbAdjust?.setCurrentDegrees(((it.originValue - it.minValue) / ((it.maxValue - ((it.maxValue + it.minValue) / 2.0f)) / 50.0f) - 50).toInt())
        })
    }
    sbAdjust?.setDegreeRange(-50, 50)
    adjustAdapter?.let {
        val currentAdjustModel: AdjustAdapter.AdjustModel =  it.currentAdjustModel

        sbAdjust.setCurrentDegrees(((currentAdjustModel.originValue - currentAdjustModel.minValue) / ((currentAdjustModel.maxValue - ((currentAdjustModel.maxValue + currentAdjustModel.minValue) / 2.0f)) / 50.0f) - 50).toInt())
//        currentAdjustModel.originValue =   abs(0 + 50)
//                .toFloat() * ((currentAdjustModel.maxValue - (currentAdjustModel.maxValue + currentAdjustModel.minValue) / 2.0f) / 50.0f) + currentAdjustModel.minValue
//        photoEditor.setAdjustFilter(it.filterConfig)
    }

    sbAdjust?.setScrollingListener(object : DegreeSeekBar.ScrollingListener {
        override fun onScrollEnd() {}
        override fun onScrollStart() {}
        override fun onScroll(i: Int) {
            adjustAdapter?.let {
                val currentAdjustModel: AdjustAdapter.AdjustModel =  it.currentAdjustModel
                currentAdjustModel.originValue =
                    abs(i + 50)
                        .toFloat() * ((currentAdjustModel.maxValue - (currentAdjustModel.maxValue + currentAdjustModel.minValue) / 2.0f) / 50.0f) + currentAdjustModel.minValue
                photoEditor.setAdjustFilter(it.filterConfig)
            }
        }
    })
    rcvAdjust?.adapter = adjustAdapter
}

fun EditFrag.initCrop() {
    addListCrop()
    cropImageView.setCropMode(CropImageView.CropMode.FREE)
    rcvEditCrop.layoutManager =  LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    rcvEditCrop?.adapter = adapterCrop
}

fun EditFrag.initSticker() {
    addButtonSticker()
    initViewPagerSticker()
    initOnClickSticker()
    stickerAlpha.setOnSeekBarChangeListener(this)
    rcvCateSticker.setUpWithAdapter(TopTabEditAdapter(vpSticker, requireActivity()))
    rcvCateSticker.setPositionThreshold(0.5f)
}

fun EditFrag.initOnClickSticker() {
    photoEditorView.onStickerOperationListener = object : StickerView.OnStickerOperationListener {
        override fun onStickerDragFinished(sticker: Sticker) {}
        override fun onStickerFlipped(sticker: Sticker) {}
        override fun onStickerTouchedDown(sticker: Sticker) {}
        override fun onStickerZoomFinished(sticker: Sticker) {}
        override fun onTouchDownForBeauty(f: Float, f2: Float) {}
        override fun onTouchDragForBeauty(f: Float, f2: Float) {}
        override fun onTouchUpForBeauty(f: Float, f2: Float) {}
        override fun onStickerAdded(sticker: Sticker) {
            stickerAlpha.show()
            stickerAlpha.progress = sticker.alpha
        }

        override fun onStickerClicked(sticker: Sticker) {
            if (sticker is TextSticker) {
                sticker.setTextColor(SupportMenu.CATEGORY_MASK)
                photoEditorView.replace(sticker)
                photoEditorView.invalidate()
            }
            stickerAlpha.show()
            stickerAlpha.progress = sticker.alpha
        }

        override fun onStickerDeleted(sticker: Sticker) {
            stickerAlpha.gone()
        }

        override fun onStickerTouchOutside() {
            stickerAlpha.gone()
        }

        override fun onStickerDoubleTapped(sticker: Sticker) {
//            if (sticker is TextSticker) {
//                sticker.setShow(false)
//              photoEditorView.setHandlingSticker(null)
//              textEditorDialogFragment = TextEditorDialogFragment.show(
//                    this@EditImageActivity,
//                    (sticker as TextSticker).getAddTextProperties()
//                )
//                val unused: TextEditorDialogFragment =
//                  textEditorDialogFragment
//              textEditor = object : TextEditor() {
//                    fun onDone(addTextProperties: AddTextProperties?) {
//                      mPhotoEditorView.getStickers()
//                            .remove(this@EditImageActivity.mPhotoEditorView.getLastHandlingSticker())
//                      mPhotoEditorView.addSticker(
//                            TextSticker(
//                                this@EditImageActivity,
//                                addTextProperties
//                            )
//                        )
//                    }
//
////                    override       fun onBackButton() {
////                      mPhotoEditorView.showLastHandlingSticker()
////                    }
//                }
////                val unused2: TextEditor = this@EditImageActivity.textEditor
////              textEditorDialogFragment.setOnTextEditorListener(this@EditImageActivity.textEditor)
//            }
        }
    }

}

fun EditFrag.initViewPagerSticker() {

    vpSticker?.adapter = object : PagerAdapter() {
        override fun getCount(): Int {
            return 13
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun destroyItem(view: View, i: Int, obj: Any) {
            (view as ViewPager).removeView(obj as View)
        }

        override fun instantiateItem(viewGroup: ViewGroup, i: Int): Any {
            val inflate =
                LayoutInflater.from(context)
                    .inflate(R.layout.sticker_items, null, false)
            val recyclerView: RecyclerView = inflate.findViewById(R.id.rv)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(
               context,
                4
            )
            when (i) {
                0 -> recyclerView.adapter = StickerAdapter(
                    context,
                    AssetUtils.lstCatFaces(),
                    i,
                    this@initViewPagerSticker
                )
                1 -> recyclerView.adapter = StickerAdapter(
                    context,
                    AssetUtils.lstCkeeks(),
                    i,
                    this@initViewPagerSticker
                )
                2 -> recyclerView.adapter = StickerAdapter(
                  context,
                    AssetUtils.lstDiadems(),
                    i,
                    this@initViewPagerSticker
                )
                3 -> recyclerView.adapter = StickerAdapter(
                    context,
                    AssetUtils.lstEyes(),
                    i,
                    this@initViewPagerSticker
                )
                4 -> recyclerView.adapter = StickerAdapter(
                    context,
                    AssetUtils.lstGiddy(),
                    i,
                    this@initViewPagerSticker
                )
                5 -> recyclerView.adapter = StickerAdapter(
                   context,
                    AssetUtils.lstGlasses(),
                    i,
                    this@initViewPagerSticker
                )
                6 -> recyclerView.adapter = StickerAdapter(
                   context,
                    AssetUtils.lstTies(),
                    i,
                    this@initViewPagerSticker
                )
                7 -> recyclerView.adapter = StickerAdapter(
                    context,
                    AssetUtils.lstHeardes(),
                    i,
                    this@initViewPagerSticker
                )
                8 -> recyclerView.adapter = StickerAdapter(
                   context,
                    AssetUtils.lstEmoj(),
                    i,
                    this@initViewPagerSticker
                )
                9 -> recyclerView.adapter = StickerAdapter(
                    context,
                    AssetUtils.lstTexts(),
                    i,
                    this@initViewPagerSticker
                )
                10 -> recyclerView.adapter = StickerAdapter(
                    context,
                    AssetUtils.lstOthers(),
                    i,
                    this@initViewPagerSticker
                )
                11 -> recyclerView.adapter = StickerAdapter(
                    context,
                    AssetUtils.lstMuscle(),
                    i,
                    this@initViewPagerSticker
                )
                12 -> recyclerView.adapter = StickerAdapter(
                    context,
                    AssetUtils.lstTatoos(),
                    i,
                    this@initViewPagerSticker
                )
            }
            viewGroup.addView(inflate)
            return inflate
        }
    }
}

fun EditFrag.onClickCrop(menuObj: MenuObj) {
    when (menuObj.id) {
        MenuType.CROP_FREE -> {
            cropImageView.setCropMode(CropImageView.CropMode.FREE)
        }
        MenuType.CROP_1_1 -> {
            cropImageView.setCustomRatio(1, 1)
        }
        MenuType.CROP_FB -> {
            cropImageView.setCustomRatio(2, 1)
        }
        MenuType.CROP_TT -> {
            cropImageView.setCustomRatio(9, 16)
        }
        MenuType.CROP_YT -> {
            cropImageView.setCustomRatio(16, 9)
        }
        MenuType.CROP_3_4 -> {
            cropImageView.setCustomRatio(3, 4)
        }
        MenuType.CROP_4_3 -> {
            cropImageView.setCustomRatio(4, 3)
        }
        else -> {
            cropImageView.setCustomRatio(1, 1)
        }
    }
}

fun EditFrag.addListCrop() {
    listCrop.clear()
    listCrop.add(
        MenuObj(
            MenuType.CROP_FREE,
            getString(R.string.crop_free),
            R.drawable.ic_crop_free
        )
    )
    listCrop.add(
        MenuObj(
            MenuType.CROP_1_1,
            getString(R.string.crop11),
            R.drawable.ic_social_instagram
        )
    )
    listCrop.add(MenuObj(MenuType.CROP_FB, getString(R.string.cropFb), R.drawable.ic_social_fb))
    listCrop.add(MenuObj(MenuType.CROP_TT, getString(R.string.cropTT), R.drawable.ic_social_tiktok))
    listCrop.add(
        MenuObj(
            MenuType.CROP_YT,
            getString(R.string.cropYT),
            R.drawable.ic_social_youtube
        )
    )
    listCrop.add(MenuObj(MenuType.CROP_3_4, getString(R.string.crop34), R.drawable.ic_frame_34))
    listCrop.add(MenuObj(MenuType.CROP_4_3, getString(R.string.crop43), R.drawable.ic_frame_43))
}


fun EditFrag.addButtonSticker() {

    context?.let {
            ctx->
        val btnRemoveSticker = BitmapStickerIcon(
            ContextCompat.getDrawable(
                ctx,
                R.drawable.sticker_ic_close_white_18dp
            ), 0, BitmapStickerIcon.REMOVE
        )
        btnRemoveSticker.iconEvent = DeleteIconEvent()
        val btnZoomSticker = BitmapStickerIcon(
            ContextCompat.getDrawable(
                ctx,
                R.drawable.sticker_ic_scale_white_18dp
            ), 3, BitmapStickerIcon.ZOOM
        )
        btnZoomSticker.iconEvent = ZoomIconEvent()
        val btnFlipSticker = BitmapStickerIcon(
            ContextCompat.getDrawable(
                ctx,
                R.drawable.sticker_ic_flip_white_18dp
            ), 1, BitmapStickerIcon.FLIP
        )
        btnFlipSticker.iconEvent = FlipHorizontallyEvent()
//    val bitmapStickerIcon4 = BitmapStickerIcon(
//        ContextCompat.getDrawable(requireContext(), R.drawable.icon_rotate),
//        3,
//        BitmapStickerIcon.ROTATE
//    )
//    bitmapStickerIcon4.iconEvent = ZoomIconEvent()
//    val bitmapStickerIcon5 = BitmapStickerIcon(
//        ContextCompat.getDrawable(requireContext(), R.drawable.icon_edit),
//        1,
//        BitmapStickerIcon.EDIT
//    )
//    bitmapStickerIcon5.iconEvent = EditTextIconEvent()

        val bitmapStickerIcon6 = BitmapStickerIcon(
            ContextCompat.getDrawable(ctx, R.drawable.icon_flip_vertical),
            2,
            BitmapStickerIcon.ALIGN_HORIZONTALLY
        )
        bitmapStickerIcon6.iconEvent = AlignHorizontallyEvent()
        photoEditorView.icons = listOf(
            btnRemoveSticker,
            btnZoomSticker,
            btnFlipSticker,
//        bitmapStickerIcon5,
//        bitmapStickerIcon4,
            bitmapStickerIcon6
        )
    }

}
