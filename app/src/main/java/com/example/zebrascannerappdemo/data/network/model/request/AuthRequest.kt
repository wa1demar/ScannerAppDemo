package com.example.zebrascannerappdemo.data.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)