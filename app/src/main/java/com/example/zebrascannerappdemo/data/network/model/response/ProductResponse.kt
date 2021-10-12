package com.example.zebrascannerappdemo.data.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val barcode: String,
    val productName: String,
    val attributeType: String? = "",
    val attributeValue: String? = "",
    val sku: String,
    val onHand: Int = 0,
    val allocated: Int = 0
)