package com.example.zebrascannerappdemo.data.storage.prefs.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val name: String,
    val locationName: String,
)