package com.example.zebrascannerappdemo.data.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterStockRequest(
    val containerBarcode: String,
    val registerReason: String,
    val products: Map<String, Int>
)