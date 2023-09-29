package com.sutech.photoeditor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sutech.photoeditor.R
import com.sutech.photoeditor.model.MenuObj
import com.sutech.photoeditor.util.isVisible
import com.sutech.photoeditor.util.setPreventDoubleClickItemScaleView
import kotlinx.android.synthetic.main.item_menu_edit.view.*

class AdapterMenu(private val listMenu: MutableList<MenuObj>,
                  private val listener: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var positionSelected = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_menu_edit, parent, false)
        return ItemImageVH(view)
    }

    override fun getItemCount(): Int = listMenu.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemImageVH).bin(position)


    }

    inner class ItemImageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bin(position: Int) {

            val menu = listMenu[position]

            itemView.tvNameItemMenu.text = menu.name
//            ImageUtils.setImage(itemView.imgItemMenu, menu.icon)
            itemView.imgItemMenu.setImageResource(menu.icon)
            itemView.imgNewMenu.isVisible(menu.isNew)

            itemView.setPreventDoubleClickItemScaleView(300) {
                    positionSelected = position
                    listener(position)
                    notifyDataSetChanged()
            }
        }
    }
}