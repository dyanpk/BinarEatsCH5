package com.hungry.binareats.utils

import android.icu.text.NumberFormat
import android.icu.util.Currency
import kotlin.math.roundToInt

fun Double.toCurrencyFormat():String{
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("IDR")
    val formattedString = format.format(this.roundToInt())
    return formattedString.replace("IDR", "Rp").replace(",", ".")
}