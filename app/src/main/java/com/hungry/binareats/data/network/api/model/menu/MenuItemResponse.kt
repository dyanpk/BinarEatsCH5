package com.hungry.binareats.data.network.api.model.menu


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.hungry.binareats.model.Menu

@Keep
data class MenuItemResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("descOfMenu")
    val descOfMenu: String?,
    @SerializedName("locationName")
    val locationName: String?,
    @SerializedName("locationUrl")
    val locationUrl: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("productImgUrl")
    val productImgUrl: String?
)

fun MenuItemResponse.toMenu() = Menu(
    id = this.id,
    nameOfMenu = this.name.orEmpty(),
    imgUrlMenu = this.productImgUrl.orEmpty(),
    priceOfMenu = this.price ?: 0.0,
    locationOfMenu = this.locationName.orEmpty(),
    descOfMenu = this.descOfMenu.orEmpty(),
    locationUrl = this.locationUrl.orEmpty()
)



fun Collection<MenuItemResponse>.toMenuList() = this.map {
    it.toMenu()
}