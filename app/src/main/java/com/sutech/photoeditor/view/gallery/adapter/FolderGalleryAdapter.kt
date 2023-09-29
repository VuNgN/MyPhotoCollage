package com.sutech.photoeditor.view.gallery.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sutech.photoeditor.R
import com.sutech.photoeditor.model.FolderGalleryObj
import com.sutech.photoeditor.util.ImageUtil
import com.sutech.photoeditor.util.setPreventDoubleClick
import kotlinx.android.synthetic.main.item_folder_gallery.view.*

class FolderGalleryAdapter(
    private val listFolder: MutableList<FolderGalleryObj>,
    val onClickItem: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_folder_gallery, parent, false)
        return ItemFolderVH(view)
    }

    override fun getItemCount(): Int {
        return listFolder.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as ItemFolderVH).bin(position, false)
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
                (holder as ItemFolderVH).bin(position, true)
            }
        }
    }

    inner class ItemFolderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bin(position: Int, isPayload: Boolean) {
            val folderObj = listFolder[position]
            folderObj.path?.let {
                ImageUtil.setImage(itemView.imgThumbFolder, it)
            }
            itemView.tvTitleFolder.text = folderObj.name
            itemView.tvSizeFolder.text  = "${folderObj.size} ${itemView.context.getString(R.string.image)} "

            itemView.setPreventDoubleClick(100) {
                onClickItem(position)
            }

        }

    }

    class InfoMessageChanged

}