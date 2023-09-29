package com.sutech.photoeditor.view.gallery.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sutech.photoeditor.R
import com.sutech.photoeditor.model.ImageGalleryObj
import com.sutech.photoeditor.util.ImageUtil
import com.sutech.photoeditor.util.setPreventDoubleClickScaleView
import kotlinx.android.synthetic.main.item_image_selected.view.*

class ImageSelectedAdapter(
    private val withItem : Int,
    private val listImage: MutableList<ImageGalleryObj>,
    val onClickRemoveItem: (position:Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_selected, parent, false)

        return ItemImageSelectedVH(view)
    }

    override fun getItemCount(): Int {
        return listImage.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as ItemImageSelectedVH).bin(position, false)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads.any { it is InfoMessageChanged }) {
                (holder as ItemImageSelectedVH).bin(position, true)
            }
        }
    }

    inner class ItemImageSelectedVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bin(position: Int, isPayload: Boolean) {
            val imageGallery = listImage[position]
            if (!isPayload) {
                itemView.layoutParams.width = withItem
                itemView.layoutParams.height = withItem
                ImageUtil.setImage(itemView.imgThumbSelected,imageGallery.pathImage)
                itemView.btnRemove.setPreventDoubleClickScaleView(100){
                    onClickRemoveItem(position)
                }
            }
        }

    }

    class InfoMessageChanged

}