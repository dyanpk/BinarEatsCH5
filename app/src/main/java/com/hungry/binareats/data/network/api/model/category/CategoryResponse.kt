package com.hungry.binareats.data.network.api.model.category


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.hungry.binareats.model.Category

@Keep
data class CategoryResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imgUrlCategory")
    val imgUrlCategory: String?,
    @SerializedName("nameOfCategory")
    val nameOfCategory: String?,
    @SerializedName("slug")
    val slug: String?
)

fun CategoryResponse.toCategory() = Category(
    id = this.id,
    imgUrlCategory = this.imgUrlCategory.orEmpty(),
    nameOfCategory = this.nameOfCategory.orEmpty(),
    slug = this.slug.orEmpty()
)

fun Collection<CategoryResponse>.toCategoryList() = this.map {
    it.toCategory()
}