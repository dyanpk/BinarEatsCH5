package com.hungry.binareats.model


data class Cart(
    var id: Int? = null,
    var menuId : Int? = null,
    val nameOfMenu: String,
    val priceOfMenu: Double,
    val imgUrlMenu: String,
    var itemQuantity: Int = 0,
    var itemNotes: String? = null,
)
