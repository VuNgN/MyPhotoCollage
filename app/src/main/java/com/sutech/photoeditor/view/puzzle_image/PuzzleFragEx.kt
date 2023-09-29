package com.sutech.photoeditor.view.puzzle_image

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.Log
import android.view.Display
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sutech.photoeditor.R
import com.sutech.photoeditor.editor.featuresfoto.puzzle.PuzzleLayout
import com.sutech.photoeditor.editor.featuresfoto.puzzle.PuzzleLayoutParser
import com.sutech.photoeditor.editor.featuresfoto.puzzle.PuzzlePiece
import com.sutech.photoeditor.editor.featuresfoto.puzzle.PuzzleView
import com.sutech.photoeditor.editor.featuresfoto.puzzle.adapter.PuzzleAdapter
import com.sutech.photoeditor.editor.featuresfoto.puzzle.adapter.PuzzleBackgroundAdapter
import com.sutech.photoeditor.model.MenuObj
import com.sutech.photoeditor.model.MenuType
import com.sutech.photoeditor.util.*
import com.sutech.photoeditor.util.AppUtil.savedImage
import com.steelkiwi.cropiwa.AspectRatio
import com.sutech.ads.AdCallback
import com.sutech.ads.AdsController
import kotlinx.android.synthetic.main.fragment_puzzle_image.*
import kotlinx.android.synthetic.main.layout_border.*
import kotlinx.android.synthetic.main.toolbar_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun PuzzleFrag.initPuzzleLayout() {
    Log.e("TAG", "loadPhoto:$pathFile ")
    if (PuzzleUtils.getPuzzleLayouts(pathFile.size) != null && PuzzleUtils.getPuzzleLayouts(pathFile.size).size > 0) {
        puzzleLayout = PuzzleUtils.getPuzzleLayouts(pathFile.size)[0]
        oldPuzzle = puzzleLayout
        photoPuzzleView?.let {
            if (puzzleLayout != null) {
                it.puzzleLayout = puzzleLayout
            }
            it.isTouchEnable = true
            it.isNeedDrawLine = false
            it.isNeedDrawOuterLine = false
            it.lineSize = 4
            it.piecePadding = 6.0f
            it.pieceRadian = 15.0f
            it.lineColor = resources.getColor(R.color.white)
            it.selectedLineColor = resources.getColor(R.color.white)
            it.handleBarColor = resources.getColor(R.color.white)
            it.setAnimateDuration(300)
            it.setOnPieceSelectedListener(object : PuzzleView.OnPieceSelectedListener {
                override fun onPieceSelected(puzzlePiece: PuzzlePiece?, i: Int) {
                }
            })
            loadPhoto()

            val defaultDisplay: Display = requireActivity().windowManager.defaultDisplay
            val point = Point()
            defaultDisplay.getSize(point)
            val layoutParams = it.layoutParams
            layoutParams.height = point.x
            layoutParams.width = point.x
            it.layoutParams = layoutParams
            currentAspect = AspectRatio(1, 1)
            it.aspectRatio = AspectRatio(1, 1)
        }
    }

}

fun PuzzleFrag.showAds() {
    if (AppUtil.isIAP|| !isNetworkConnected()) {
        layoutAdsEditCollage    .gone()
    }else{
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    Log.e("TAG", "onStateChanged: " + AppConstant.isShowingOpenAd)
                    layoutAdsEditCollage  ?.postDelayed({
                        if (!AppConstant.isShowingOpenAd) {
                            layoutAdsEditCollage?.show()
                        }
                    }, 1000)
                } else if (event == Lifecycle.Event.ON_STOP) {
                    layoutAdsEditCollage?.inv()
                }
            }
        })
        AdsController.getInstance().loadAndShow(
            spaceName = "banner_collage",
            layout = layoutAdsEditCollage,
            adCallback = object : AdCallback {
                override fun onAdClick() {
                    super.onAdClick()
                    AppConstant.checkInter = true
                }

                override fun onAdShow(network: String, adtype: String) {
                    viewGoneAdEditCollage?.gone()
                }

                override fun onAdClose(adType: String) {

                }

                override fun onAdFailToLoad(messageError: String?) {
                    layoutAdsEditCollage?.gone()
                }

                override fun onAdOff() {

                }

            })

    }
}

fun PuzzleFrag.loadPhoto() {
    CoroutineScope(Dispatchers.IO).launch {
        for (image in pathFile) {
            Glide.with(requireContext())
                .asBitmap()
                .override(400, 400)
                .load(image)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onResourceReady(
                        bitmap: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        listBitmap.add(bitmap)
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                Log.e("TAG", "loadPhoto:$bitmap ")
                                photoPuzzleView?.addPiece(bitmap)
                            } catch (e: Exception) {
                                Log.e("TAG", "loadPhoto: ")
                            }
                        }
                    }

                })


        }
    }
}

fun PuzzleFrag.initOnClick() {
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        if (canBack) {
            onClickBack()
        }
    }
    Handler().postDelayed({
        canBack = true
        btnBackEdit?.setPreventDoubleClickItemScaleView {
            logEvent("EditCollageScr_IconBack_Clicked")
            onBackPress()
        }
        btnBackPuzzle?.setPreventDoubleClickItemScaleView {
            onClickBack()
        }
    }, 1000)

    logEvent("EditCollageScr_Show")


    btnDoneEdit?.setPreventDoubleClickItemScaleView {
        logEvent("EditCollageScr_IconSave_Clicked")
        DialogUtil.showDialogLoading(requireContext())
        photoPuzzleView?.setHandlingPiece(null)
        photoPuzzleView?.setLocked(true)
        photoPuzzleView?.isTouchEnable = true
        ViewToBitmap.of(photoPuzzleView).toJPG()
            .setOnSaveResultListener { isSaved, path, uri ->
                DialogUtil.cancelDialogLoading()
                if (uri != null) {
                    savedImage = uri
                    AppUtil.countAds ++
                    if (AppUtil.isIAP||AppUtil.countAds%2==0|| !isNetworkConnected()) {

                        gotoFrag(
                            R.id.puzzleFrag,
                            R.id.action_puzzleFrag_to_saveAndShareFrag
                        )
                    } else {
                        showAdInter("inter_collage", 8000,  getString(R.string.loading_ad_2), {
                            gotoFrag(
                                R.id.puzzleFrag,
                                R.id.action_puzzleFrag_to_saveAndShareFrag
                            )

                        }, {

                        }, {
                            gotoFrag(
                            R.id.puzzleFrag,
                            R.id.action_puzzleFrag_to_saveAndShareFrag
                        )
                        }, {

                        })

                    }


                }
            }.save(requireContext())
    }

    btnSavePuzzle?.setPreventDoubleClickItemScaleView {
        when (menuType) {
            MenuType.PUZZLE_MAIN -> {

            }
            MenuType.PUZZLE_LAYOUT -> {
                oldPuzzlePosition = newPuzzlePosition
                oldPuzzle = newPuzzle
                menuType = MenuType.PUZZLE_MAIN
                onClickMenuMain()
            }
            MenuType.PUZZLE_RATIO -> {
                currentAspect = newAspect
                menuType = MenuType.PUZZLE_MAIN
                onClickMenuMain()
            }

            MenuType.PUZZLE_BACKGROUND -> {
                oldBackgroundPuzzle = newBackgroundPuzzle
                oldBackground = newBackground
                menuType = MenuType.PUZZLE_MAIN
                onClickMenuMain()
            }

            MenuType.PUZZLE_BORDER -> {

                oldBorder = newBorder
                oldRadius = newRadius
                menuType = MenuType.PUZZLE_MAIN
                onClickMenuMain()
            }

            else -> {
                menuType = MenuType.PUZZLE_MAIN
                onClickMenuMain()
            }
        }
    }
}

fun PuzzleFrag.initBorder() {
    sbRadius.setOnSeekBarChangeListener(this)
    sbLine.setOnSeekBarChangeListener(this)
}

fun PuzzleFrag.onClickBack() {
    when (menuType) {
        MenuType.PUZZLE_MAIN -> {
//            if (mInterstitialAd.isLoaded) {
//                mInterstitialAd.show()
//            } else {
            onBackPress()
//            }
//            if(AppUtil.isIAP){
//                onBackPress()
//            }else{
//                mInterstitialAd.adListener = object : AdListener() {
//                override fun onAdLoaded() {
//                    // dialog.dismiss()
//
//                }
//
//                override fun onAdFailedToLoad(errorCode: Int) {
//                    onBackPress()
//
//                }
//
//                override fun onAdOpened() {
//                    onBackPress()
//                }
//
//                override fun onAdClicked() {
//                    // Code to be executed when the user clicks on an ad.
//                }
//
//                override fun onAdLeftApplication() {
//                    // Code to be executed when the user has left the app.
//                }
//
//                override fun onAdClosed() {
//
//                    // Code to be executed when the interstitial ad is closed.
//                }
//            }
//            }


        }
        MenuType.PUZZLE_LAYOUT -> {
            oldPuzzle?.let{
                if (it != newPuzzle) {
                    updatePuzzleLayout(it)
                    puzzleAdapter?.selectedIndex = oldPuzzlePosition
                    puzzleAdapter?.notifyDataSetChanged()
                }
            }
            menuType = MenuType.PUZZLE_MAIN
            onClickMenuMain()
        }
        MenuType.PUZZLE_RATIO -> {
            newAspect = currentAspect
            cropPuzzle(newAspect)
            menuType = MenuType.PUZZLE_MAIN
            onClickMenuMain()
        }

        MenuType.PUZZLE_BORDER
        -> {
            sbLine?.progress = oldBorder
            sbRadius?.progress = oldRadius
            menuType = MenuType.PUZZLE_MAIN
            onClickMenuMain()
        }
        MenuType.PUZZLE_BACKGROUND
        -> {
            puzzleBackgroundAdapter?.selectedSquareIndex = oldBackgroundPuzzle
            oldBackground?.let {
                if (it.isColor) {
                    photoPuzzleView.setBackgroundColor(it.drawableId)
                    photoPuzzleView.backgroundResourceMode = 0
                }
                puzzleBackgroundAdapter?.notifyDataSetChanged()
            }
            menuType = MenuType.PUZZLE_MAIN
            onClickMenuMain()
        }

        else -> {
            menuType = MenuType.PUZZLE_MAIN
            onClickMenuMain()
        }
    }
}

fun PuzzleFrag.onClickMenuMain() {
    hideAllMenu()
    menuPuzzle.layoutParams.height = context?.convertDpToPx(50)!!
    btnSavePuzzle.show()
//    ImageUtil.setImage(btnSavePuzzle, R.drawable.ic_tick, 1f)
//    ImageUtil.setImage(btnBackPuzzle, R.drawable.ic_close, 1f)
    when (menuType) {
        MenuType.PUZZLE_MAIN -> {
            menuPuzzle.layoutParams.height = 0
//            ImageUtil.setImage(btnBackPuzzle, R.drawable.ic_back, 1f)
//            tvTitleMenuPuzzle?.text = getString(R.string.edit_layout)
//            ImageUtil.setImage(btnSavePuzzle, R.drawable.ic_saved_home, 1f)
            rcvMenuPuzzleMain.slideShowToFade(durationAnimMenu)
        }
        MenuType.PUZZLE_BORDER -> {
            logEvent("EditCollageScr_IconBorder_Clicked")
            tvTitleMenuPuzzle?.text = getString(R.string.border)
            layoutBorder.slideShowToFade(durationAnimMenu)
        }
        MenuType.PUZZLE_RATIO -> {
            logEvent("EditCollageScr_IconRatio_Clicked")
            tvTitleMenuPuzzle?.text = getString(R.string.ratio)
            rcvPuzzleCrop.slideShowToFade(durationAnimMenu)
        }
        MenuType.PUZZLE_LAYOUT -> {
            logEvent("EditCollageScr_IconLayout_Clicked")
            tvTitleMenuPuzzle?.text = getString(R.string.layout)
            rcvPuzzleLayout.slideShowToFade(durationAnimMenu)
        }
        MenuType.PUZZLE_BACKGROUND -> {
            logEvent("EditCollScr_IconBackgro_Clicked")
            tvTitleMenuPuzzle?.text = getString(R.string.label_background)
            rcvPuzzleBackGround.slideShowToFade(durationAnimMenu)
        }

        else -> {
        }
    }
}


fun PuzzleFrag.hideAllMenu() {
    rcvMenuPuzzleMain.gone()
    rcvPuzzleLayout.gone()
    rcvPuzzleCrop.gone()
    rcvPuzzleBackGround.gone()
    layoutBorder.gone()
}


fun PuzzleFrag.initMenuPuzzleMain() {
    if (listMenuEditMain.isEmpty()) {
        addMenuEditMain()
    }
    rcvMenuPuzzleMain.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    rcvMenuPuzzleMain.adapter = adapterMenuEditMain
}

fun PuzzleFrag.initMenuPuzzleLayout() {
    puzzleAdapter = PuzzleAdapter()
    puzzleAdapter?.refreshData(
        PuzzleUtils.getPuzzleLayouts(pathFile.size),
        null as List<Bitmap?>?
    )
    puzzleAdapter?.setOnItemClickListener { puzzleLayout, i ->
        newPuzzlePosition = i
        newPuzzle = PuzzleLayoutParser.parse(puzzleLayout?.generateInfo())
        updatePuzzleLayout(newPuzzle!!)
    }

    rcvPuzzleLayout?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    rcvPuzzleLayout?.adapter = puzzleAdapter

}

fun PuzzleFrag.updatePuzzleLayout(parse: PuzzleLayout) {
    photoPuzzleView?.let {
        puzzleLayout?.radian = it.pieceRadian
        puzzleLayout?.padding = it.piecePadding
        it.updateLayout(parse)
    }
}

fun PuzzleFrag.initMenuBackground() {
    if (listMenuEditMain.isEmpty()) {
        addMenuEditMain()
    }
    puzzleBackgroundAdapter = PuzzleBackgroundAdapter(requireContext(), this)
    rcvPuzzleBackGround?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    rcvPuzzleBackGround?.adapter = puzzleBackgroundAdapter
}

fun PuzzleFrag.addMenuEditMain() {
    listMenuEditMain.clear()


    listMenuEditMain.add(
        MenuObj(
            MenuType.PUZZLE_LAYOUT,
            getString(R.string.layout),
            R.drawable.collage
        )
    )
    listMenuEditMain.add(
        MenuObj(
            MenuType.PUZZLE_BORDER,
            getString(R.string.border),
            R.drawable.ic_border
        )
    )
    listMenuEditMain.add(
        MenuObj(
            MenuType.PUZZLE_RATIO,
            getString(R.string.ratio),
            R.drawable.ic_crop_11
        )
    )
    listMenuEditMain.add(
        MenuObj(
            MenuType.PUZZLE_BACKGROUND,
            getString(R.string.label_background),
            R.drawable.background_icon
        )
    )
}

fun PuzzleFrag.initCrop() {
    addListCrop()
    rcvPuzzleCrop.layoutManager =
        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    rcvPuzzleCrop.adapter = adapterCrop
}

fun PuzzleFrag.onClickCrop(menuObj: MenuObj) {
    when (menuObj.id) {
        MenuType.CROP_FREE -> {
            photoPuzzleView?.aspectRatio = currentAspect
        }
        MenuType.CROP_1_1 -> {
            newAspect = AspectRatio(1, 1)
        }
        MenuType.CROP_FB -> {
            newAspect = AspectRatio(2, 1)
        }
        MenuType.CROP_TT -> {
            newAspect = AspectRatio(9, 16)
        }
        MenuType.CROP_YT -> {
            newAspect = AspectRatio(16, 9)
        }
        MenuType.CROP_3_4 -> {
            newAspect = AspectRatio(3, 4)
        }
        MenuType.CROP_4_3 -> {
            newAspect = AspectRatio(4, 3)
        }
        else -> {
            cropPuzzle(AspectRatio(1, 1))
        }
    }
    cropPuzzle(newAspect)
}

fun PuzzleFrag.cropPuzzle(aspectRatio: AspectRatio) {
    photoPuzzleView?.let {

        val defaultDisplay: Display = requireActivity().windowManager.defaultDisplay
        val point = Point()
        defaultDisplay.getSize(point)
        val calculateWidthAndHeight: IntArray = calculateWidthAndHeight(aspectRatio, point)
        it.layoutParams = ConstraintLayout.LayoutParams(
            calculateWidthAndHeight[0],
            calculateWidthAndHeight[1]
        )
        val constraintSet = ConstraintSet()
        constraintSet.clone(wrapPuzzleView)
        constraintSet.connect(it.id, 3, this.wrapPuzzleView.id, 3, 0)
        constraintSet.connect(it.id, 1, this.wrapPuzzleView.id, 1, 0)
        constraintSet.connect(it.id, 4, this.wrapPuzzleView.id, 4, 0)
        constraintSet.connect(it.id, 2, this.wrapPuzzleView.id, 2, 0)
        constraintSet.applyTo(wrapPuzzleView)
        it.aspectRatio = aspectRatio
    }
}


private fun PuzzleFrag.calculateWidthAndHeight(
    aspectRatio: AspectRatio,
    point: Point
): IntArray {
    val height: Int = this.wrapPuzzleView.height
    if (aspectRatio.height > aspectRatio.width) {
        val ratio = (aspectRatio.ratio * height.toFloat()).toInt()
        return if (ratio < point.x) {
            intArrayOf(ratio, height)
        } else intArrayOf(point.x, (point.x.toFloat() / aspectRatio.ratio).toInt())
    }
    val ratio2 = (point.x.toFloat() / aspectRatio.ratio).toInt()
    return if (ratio2 > height) {
        intArrayOf((height.toFloat() * aspectRatio.ratio).toInt(), height)
    } else intArrayOf(point.x, ratio2)
}

fun PuzzleFrag.addListCrop() {
    listCrop.clear()
//    listCrop.add(  MenuObj(MenuType.CROP_FREE, getString(R.string.crop_free),  R.drawable.ic_crop_free ))
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


