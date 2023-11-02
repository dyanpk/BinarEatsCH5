package com.hungry.binareats.data.network.api.model.order

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val data: Any?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?
)
