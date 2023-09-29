package com.sutech.photoeditor.base

import android.app.Application
import android.util.Log
import com.sutech.photoeditor.database.DataStore
  import com.sutech.photoeditor.widget.TypeFaceUtil

class AppController : Application() {
    // true if user is updated
    var themeId = 1;
    private object Holder {
        val INSTANCE = AppController()
    }

    override fun onCreate() {
        super.onCreate()
        DataStore.init(this)
        TypeFaceUtil.instant!!.initTypeFace(this)
//        if (DataStore.getTheme()!=-1){
//            setThemeApp(DataStore.getTheme())
//        }else{
            setThemeApp(0)
//        }
    }

    fun setThemeApp(position: Int) {
        themeId =position
        Log.e("TAG", "setThemeApp: $position" )
        when (position) {
            0 -> {
                setTextColor()
            }
            1 -> {
                setTextColor("#F2F2F2", "#febd00", "#F2F2F2", "#163DFF", "#000000", "#febd00")
            }
            2 -> {
                setTextColor("#F2F2F2", "#febd00", "#F2F2F2", "#163DFF", "#304FFE", "#000000")
            }
            3 -> {
                setTextColor("#F2F2F2", "#febd00", "#F2F2F2", "#163DFF", "#D50000", "#febd00")
            }
            4 -> {
                setTextColor("#F2F2F2", "#febd00", "#F2F2F2", "#163DFF", "#00C853", "#febd00")
            }
            5 -> {
                setTextColor("#F2F2F2", "#febd00", "#F2F2F2", "#163DFF", "#AA00FF", "#000000")
            }
            6 -> {
                setTextColor("#000000", "#febd00", "#F2F2F2", "#163DFF", "#FFAB00", "#000000")
            }
            7 -> {
                setTextColor("#000000", "#febd00", "#F2F2F2", "#163DFF", "#FF6D00", "#000000")
            }
            8 -> {
                setTextColor("#000000", "#febd00", "#000000", "#163DFF", "#F67FB5", "#F2F2F2")
            }
        }
    }

    fun setTextColor(
        textColor: String? = "#000000",
        colorSelected: String? = "#000000",
        colorEditTex: String? = "#252525",
        colorTextSecond: String? = "#000000",
        backgroundColor: String? = "#F2F2F2",
        iconColor: String? = "#252525"
    ) {
        ColorUtil.instant!!.initColor(
            textColor,
            colorSelected,
            colorEditTex,
            colorTextSecond,
            backgroundColor,
            iconColor
        )

    }

    companion object {
        @JvmStatic
        fun getInstance(): AppController {
            return Holder.INSTANCE
        }
    }

}