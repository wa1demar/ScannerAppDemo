package com.example.zebrascannerappdemo.data.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class EmptyResponse(
    val message: String? = null
)