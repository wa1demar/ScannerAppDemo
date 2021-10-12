package com.example.zebrascannerappdemo.data.storage.prefs.model

import kotlinx.serialization.Serializable

@Serializable
data class CredentialsDto(
    val username: String,
    val password: String
)