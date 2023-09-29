package com.sutech.photoeditor.view.saved.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sutech.photoeditor.R
import com.sutech.photoeditor.model.ImageGalleryObj
import com.sutech.photoeditor.util.*
import kotlinx.android.synthetic.main.item_saved.view.*

class SavedAdapter(
    private val listImage: MutableList<ImageGalleryObj>,
    val onClickItem: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved, parent, false)

        return ItemImageVH(view)
    }

    override fun getItemCount(): Int {
        return listImage.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemImageVH).bin(position)
    }


    inner class ItemImageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bin(position: Int ) {
            val imageGallery = listImage[position]

                imageGallery.positionInAdapter = position
//                    ImageUtil.setImage(itemView.imgSaved, imageGallery.pathImage)

                    Glide.with(itemView.context)
                        .asBitmap()
                        .load(imageGallery.pathImage)
                        .thumbnail(0.01f)
                        .override(300,300)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onLoadCleared(placeholder: Drawable?) {
                            }

                            override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
//                                val width = bitmap.width.toDouble()
//                                val height = bitmap.height.toDouble()
//                                itemView.imgSaved.aspectRatio =   height/width
                                itemView.imgSaved.setImageBitmap(bitmap)
                            }

                        })

            itemView.setPreventDoubleClickItem(500) {
                onClickItem(position)
            }


        }

    }


}