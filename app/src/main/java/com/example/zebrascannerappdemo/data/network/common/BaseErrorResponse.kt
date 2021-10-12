package com.example.zebrascannerappdemo.data.network.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseErrorResponse(
    var success: Boolean = false,
    @SerialName("error")
    var error: ErrorResponse? = null,
    var message: String? = null
)

@Serializable
data class ErrorResponse(
    val code: Int,
    val message: String?
)