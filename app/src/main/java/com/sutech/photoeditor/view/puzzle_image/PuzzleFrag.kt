package com.sutech.photoeditor.view.puzzle_image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.widget.SeekBar
import com.sutech.photoeditor.R
import com.sutech.photoeditor.adapter.AdapterMenu
import com.sutech.photoeditor.base.BaseFragment
import com.sutech.photoeditor.editor.featuresfoto.puzzle.PuzzleLayout
import com.sutech.photoeditor.editor.featuresfoto.puzzle.adapter.PuzzleAdapter
import com.sutech.photoeditor.editor.featuresfoto.puzzle.adapter.PuzzleBackgroundAdapter
import com.sutech.photoeditor.model.MenuObj
import com.sutech.photoeditor.model.MenuType
import com.sutech.photoeditor.util.AppUtil
import com.steelkiwi.cropiwa.AspectRatio
import kotlinx.android.synthetic.main.fragment_puzzle_image.*
import org.wysaid.nativePort.CGENativeLibrary
import org.wysaid.nativePort.CGENativeLibrary.LoadImageCallback
import java.io.IOException

class PuzzleFrag : BaseFragment(R.layout.fragment_puzzle_image),
    SeekBar.OnSeekBarChangeListener, PuzzleBackgroundAdapter.BackgroundChangeListener {
    var durationAnimMenu = 300L
    var currentBitmap: Bitmap? = null
    var pathFile: ArrayList<Uri> = ArrayList()
    var puzzleLayout: PuzzleLayout? = null

    var canBack: Boolean = false
    var currentAspect: AspectRatio = AspectRatio(1, 1)
    var newAspect: AspectRatio = AspectRatio(1, 1)
    val listBitmap = arrayListOf<Bitmap>()

    var menuType: MenuType = MenuType.PUZZLE_MAIN

    val listMenuEditMain = mutableListOf<MenuObj>()
    val adapterMenuEditMain by lazy {
        AdapterMenu(listMenuEditMain) {
            menuType = listMenuEditMain[it].id
            onClickMenuMain()
        }
    }
    val listCrop = mutableListOf<MenuObj>()
    val adapterCrop by lazy {
        AdapterMenu(listCrop) {
            onClickCrop(listCrop[it])
        }
    }
    var puzzleAdapter: PuzzleAdapter? = null
    var puzzleBackgroundAdapter: PuzzleBackgroundAdapter? = null

    var newBorder:Int  =0
    var newRadius:Int  =0

    var oldBorder:Int  =0
    var oldRadius:Int  =0


    var oldBackgroundPuzzle:Int  =0
    var newBackgroundPuzzle:Int  =0

    var oldBackground:PuzzleBackgroundAdapter.SquareView? = PuzzleBackgroundAdapter.SquareView(Color.parseColor("#000000"), "White", true)
    var newBackground:PuzzleBackgroundAdapter.SquareView? =null

    var oldPuzzlePosition:Int? = 0
    var newPuzzlePosition:Int? = 0

    var oldPuzzle: PuzzleLayout? = null
    var newPuzzle: PuzzleLayout? = null
    override fun onCreatedView() {

        CGENativeLibrary.setLoadImageCallback(object : LoadImageCallback {
            override fun loadImage(name: String, arg: Any?): Bitmap? {
                return try {

                    context?.let {
                            ctx->

                    }
                    BitmapFactory.decodeStream(requireContext().assets.open(name))
                } catch (unused: IOException) {
                    null
                }
            }

            override fun loadImageOK(bmp: Bitmap?, arg: Any?) {
                bmp?.recycle()
            }
        }, null)

        val listImage = AppUtil.listImageSelected
        for (image in listImage) {
            pathFile.add(image.pathImage)
        }
        showAds()
        initPuzzleLayout()
        initOnClick()
        initBorder()
        initMenuPuzzleMain()
        initMenuPuzzleLayout()
        initMenuBackground()
        initCrop()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        seekBar?.let {
            when (it.id) {
                R.id.sbLine -> {
                    newBorder =progress
                    photoPuzzleView.piecePadding = progress.toFloat()
                }
                R.id.sbRadius -> {
                    newRadius=progress
                    photoPuzzleView.pieceRadian = progress.toFloat()
                }
                else -> {
                }

            }
        }
    }


    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onBackgroundSelected(squareView: PuzzleBackgroundAdapter.SquareView?) {
        newBackground = squareView
        newBackgroundPuzzle = puzzleBackgroundAdapter!!.selectedSquareIndex
        if (squareView!!.isColor) {

            photoPuzzleView.setBackgroundColor(squareView.drawableId)
            photoPuzzleView.backgroundResourceMode = 0
        }
    }

}