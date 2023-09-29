package com.sutech.photoeditor.model

data class MenuObj(
        var id: MenuType = MenuType.EDIT_MAIN,
        var name: String = "",
        var icon: Int = 0,
        var isNew: Boolean = false

)