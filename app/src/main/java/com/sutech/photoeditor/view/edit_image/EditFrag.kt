package com.sutech.photoeditor.view.edit_image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.widget.SeekBar
import com.sutech.photoeditor.R
import com.sutech.photoeditor.adapter.AdapterMenu
import com.sutech.photoeditor.base.BaseFragment
import com.sutech.photoeditor.editor.featuresfoto.adjust.AdjustAdapter
import com.sutech.photoeditor.editor.featuresfoto.adjust.AdjustListener
import com.sutech.photoeditor.editor.featuresfoto.sticker.adapter.StickerAdapter
import com.sutech.photoeditor.editor.filterscolor.FilterListener
import com.sutech.photoeditor.editor.filterscolor.FilterViewAdapter
import com.sutech.photoeditor.editor.sticker.DrawableSticker
import com.sutech.photoeditor.model.ImageGalleryObj
import com.sutech.photoeditor.model.MenuObj
import com.sutech.photoeditor.model.MenuType
import com.sutech.photoeditor.widget.customview.photo.PhotoEditor
import kotlinx.android.synthetic.main.fragment_edit_image.*
import kotlinx.android.synthetic.main.layout_adjust.*
import kotlinx.android.synthetic.main.layout_filter.*
import kotlinx.android.synthetic.main.layout_over_play.*
import kotlinx.android.synthetic.main.toolbar_edit.*
import org.wysaid.nativePort.CGENativeLibrary
import org.wysaid.nativePort.CGENativeLibrary.LoadImageCallback
import java.io.IOException


class EditFrag : BaseFragment(R.layout.fragment_edit_image), StickerAdapter.OnClickStickerListener,
    SeekBar.OnSeekBarChangeListener,
    FilterListener {
    var durationAnimMenu = 300L
    var currentBitmap: Bitmap? = null
    var imageObj: ImageGalleryObj? = null

    var listBitmapFilter = mutableListOf<Bitmap>()
    var listBitmapOverplay = mutableListOf<Bitmap>()


    var filterViewAdapter: FilterViewAdapter? = null
    var overplayViewAdapter: FilterViewAdapter? = null
    var menuType: MenuType = MenuType.EDIT_MAIN
    val photoEditor: PhotoEditor by lazy {
        PhotoEditor.Builder(requireContext(), photoEditorView).setPinchTextScalable(true).build()
    }
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
    var adjustAdapter:AdjustAdapter?=null

    var canBack:Boolean =false
    override fun onCreatedView() {
        CGENativeLibrary.setLoadImageCallback(object : LoadImageCallback {
            override fun loadImage(name: String, arg: Any?): Bitmap? {
                return try {
                    context?.let {
                        BitmapFactory.decodeStream(it.assets.open(name)) }
                } catch (unused: IOException) {
                    null
                }
            }

            override fun loadImageOK(bmp: Bitmap?, arg: Any?) {
                bmp?.recycle()
            }
        }, null)
        tvTitleEdit?.text
        showAds()
        setBackgroundImage()
        initOnClick()
        initMenuEditMain()
        initSticker()
        initCrop()
    }
    override fun addSticker(bitmap: Bitmap?) {
        photoEditorView.addSticker(DrawableSticker(BitmapDrawable(resources, bitmap)))

    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        seekBar?.let {
            when (it.id) {

                R.id.stickerAlpha -> {
                    photoEditorView?.currentSticker?.alpha = progress
                }
                R.id.sbFilter -> {
                    photoEditorView?.setFilterIntensity(progress.toFloat() / 100.0f)
                }
                R.id.sbOverplay -> {
                    photoEditorView?.setFilterIntensity(progress.toFloat() / 100.0f)
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

    override fun onFilterSelected(str: String?) {

        photoEditor.setFilterEffect(str)
        sbFilter.progress = 100
        sbOverplay.progress = 70
        if (menuType == MenuType.EDIT_OVERPLAY) {
            photoEditorView.glSurfaceView.setFilterIntensity(0.7f)
        }
    }
}