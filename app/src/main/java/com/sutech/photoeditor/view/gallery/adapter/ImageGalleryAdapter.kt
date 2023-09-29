package com.sutech.photoeditor.view.gallery.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sutech.photoeditor.R
import com.sutech.photoeditor.model.ImageGalleryObj
import com.sutech.photoeditor.util.ImageUtil
import com.sutech.photoeditor.util.gone
import com.sutech.photoeditor.util.setPreventDoubleClickItemScaleView
import com.sutech.photoeditor.util.show
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.item_image_2.view.*

class ImageGalleryAdapter(
    private val isChoose: Boolean = false,
    private val listImage: MutableList<ImageGalleryObj>,
    val onClickItem: (position: Int) -> Unit,
    val onClickRemoveItem: (position: Int) -> Unit,
    val clearAllItem: (oldPosition: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var oldPositionSelected = -1
    val hashMap: HashMap<Long, ImageGalleryObj> = HashMap<Long, ImageGalleryObj>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isChoose) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image_2, parent, false)
            ItemImageVH2(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false)
            ItemImageVH(view)
        }

    }

    override fun getItemCount(): Int {
        return listImage.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isChoose) {
            (holder as ItemImageVH2).bin(position, false)
        } else {
            (holder as ItemImageVH).bin(position, false)
        }
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
                if (isChoose) {
                    (holder as ItemImageVH2).bin(position, true)
                } else {
                    (holder as ItemImageVH).bin(position, true)
                }
            }
        }
    }

    inner class ItemImageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bin(position: Int, isPayload: Boolean) {
            val imageGallery = listImage[position]
            if (!isPayload) {
                imageGallery.positionInAdapter = position
                ImageUtil.setImage(itemView.imgThumb, imageGallery.pathImage)

            }
            val isSelected = hashMap[imageGallery.id]
            if (isSelected != null) {
                imageGallery.countSelected = isSelected.countSelected
            } else {
                imageGallery.countSelected = 0
            }
            if (imageGallery.countSelected > 0) {
//                itemView.bgImageSelected.show()
//                itemView.btnRemove.show()
                itemView.tvSelected.show()
                itemView.tvSelected.text = imageGallery.countSelected.toString()
            } else {
//                itemView.btnRemove.gone()
//                itemView.bgImageSelected.gone()
                itemView.tvSelected.gone()
            }
            itemView.setPreventDoubleClickItemScaleView(100) {
                onClickItem(position)
            }
            itemView.btnRemove.setPreventDoubleClickItemScaleView(100) {
                onClickRemoveItem(position)
            }

        }

    }

    inner class ItemImageVH2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bin(position: Int, isPayload: Boolean) {
            val imageGallery = listImage[position]
            if (!isPayload) {

                imageGallery.positionInAdapter = position
                ImageUtil.setImage(itemView.imgThumb2, imageGallery.pathImage)

            }
            val isSelected = hashMap[imageGallery.id]
            if (isSelected != null) {
                imageGallery.countSelected = isSelected.countSelected
            } else {
                imageGallery.countSelected = 0
            }
            if (imageGallery.countSelected > 0) {
                itemView.bgImageSelected2.show()
                itemView.imgSelected2.show()
            } else {
                itemView.bgImageSelected2.gone()
                itemView.imgSelected2.gone()
            }

            itemView.setPreventDoubleClickItemScaleView(100) {
                hashMap.clear()
                clearAllItem(oldPositionSelected)
                onClickItem(position)
                oldPositionSelected = position


            }
            itemView.bgImageSelected2.setPreventDoubleClickItemScaleView(100) {
                onClickRemoveItem(position)
            }

        }

    }

    class InfoMessageChanged

}