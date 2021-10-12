package com.example.zebrascannerappdemo.data.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(
    val message: String? = null
)