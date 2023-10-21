package com.hungry.binareats.model

import java.util.UUID


data class Category(
    val id: Int?,
    val imgUrlCategory: String,
    val nameOfCategory: String,
    val slug: String?
)
