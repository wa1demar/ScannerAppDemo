package com.example.zebrascannerappdemo.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val barcode: String,
    val productName: String,
    val quantity: Int = 0
) : Parcelable