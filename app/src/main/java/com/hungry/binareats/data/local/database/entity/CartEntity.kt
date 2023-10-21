package com.hungry.binareats.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carts")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "menu_id")
    var menuId: Int? = null,
    @ColumnInfo(name = "name_of_menu")
    val nameOfMenu: String,
    @ColumnInfo(name = "price_of_menu")
    val priceOfMenu: Double?,
    @ColumnInfo(name = "img_url_menu")
    val imgUrlMenu: String,
    @ColumnInfo(name = "item_quantity")
    var itemQuantity: Int = 0,
    @ColumnInfo(name = "item_notes")
    var itemNotes: String? = null,
)
