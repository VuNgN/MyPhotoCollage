package com.sutech.photoeditor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.sutech.photoeditor.R
import com.sutech.photoeditor.util.setPreventDoubleClickItemScaleView
import kotlinx.android.synthetic.main.item_color.view.*

class ChooseColorAdapter(
    private val listColor: MutableList<Int>,
    private val onClickItem: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemColorVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        )
    }

    override fun getItemCount(): Int = listColor.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemColorVH).onBinData(position)
    }

    private inner class ItemColorVH(view: View) : RecyclerView.ViewHolder(view) {
        fun onBinData(position: Int) {
         itemView.imgColor.setBackgroundColor( getColor(itemView.context,listColor[position]))
            itemView.setPreventDoubleClickItemScaleView(500) {
                onClickItem(position)
            }
        }
    }
}