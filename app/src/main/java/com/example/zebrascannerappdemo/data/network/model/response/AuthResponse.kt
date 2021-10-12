package com.example.zebrascannerappdemo.data.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("userinfo")
    val userInfo: UserInfoResponse,
)