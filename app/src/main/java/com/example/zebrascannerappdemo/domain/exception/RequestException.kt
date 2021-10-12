package com.example.zebrascannerappdemo.domain.exception

class RequestError(
    override val message: String? = null,
    override val cause: Exception? = null
) : Exception(message)
