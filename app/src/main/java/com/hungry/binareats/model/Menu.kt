package com.hungry.binareats.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Menu(
    val id: Int,
    val nameOfMenu: String,
    val imgUrlMenu: String,
    val priceOfMenu: Double?,
    val descOfMenu: String,
    val locationOfMenu : String,
    val locationUrl : String
) : Parcelable
