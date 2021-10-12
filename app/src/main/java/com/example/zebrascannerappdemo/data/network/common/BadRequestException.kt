package com.example.zebrascannerappdemo.data.network.common

class BadRequestException(
    val statusCode: Int = -1,
    val statusMessage: String? = null,
    val url: String? = null,
    cause: Throwable? = null
) : Exception(null, cause)
