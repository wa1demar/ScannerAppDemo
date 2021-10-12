package com.example.zebrascannerappdemo.data.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SignIntRequest(
    val authentication: AuthRequest
) {
    constructor(username: String, password: String) : this(AuthRequest(username, password))
}