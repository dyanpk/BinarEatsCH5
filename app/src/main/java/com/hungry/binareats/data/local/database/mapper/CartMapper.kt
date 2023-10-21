package com.hungry.binareats.data.local.database.mapper

import com.hungry.binareats.data.local.database.entity.CartEntity
import com.hungry.binareats.model.Cart

fun CartEntity?.toCart() = Cart(
    id = this?.id ?: 0,
    menuId = this?.menuId ?: 0,
    nameOfMenu = this?.nameOfMenu.orEmpty(),
    priceOfMenu = this?.priceOfMenu ?: 0.0,
    imgUrlMenu = this?.imgUrlMenu.orEmpty(),
    itemQuantity = this?.itemQuantity ?: 0,
    itemNotes = this?.itemNotes.orEmpty()
)

fun Cart?.toCartEntity() = CartEntity(
    id = this?.id ?: 0,
    menuId = this?.menuId ?: 0,
    nameOfMenu = this?.nameOfMenu.orEmpty(),
    priceOfMenu = this?.priceOfMenu ?: 0.0,
    imgUrlMenu = this?.imgUrlMenu.orEmpty(),
    itemQuantity = this?.itemQuantity ?: 0,
    itemNotes = this?.itemNotes.orEmpty()
)

fun List<CartEntity?>.toCartList() = this.map { it.toCart() }