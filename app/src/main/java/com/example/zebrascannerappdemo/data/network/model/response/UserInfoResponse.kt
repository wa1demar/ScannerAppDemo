package com.example.zebrascannerappdemo.data.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val name: String,
    val locationName: String,
)