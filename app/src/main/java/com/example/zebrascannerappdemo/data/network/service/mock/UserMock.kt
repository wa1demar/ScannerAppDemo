package com.example.zebrascannerappdemo.data.network.service.mock

import com.example.zebrascannerappdemo.data.network.model.response.AuthResponse
import com.example.zebrascannerappdemo.data.network.model.response.UserInfoResponse

object UserMock {
    fun create(): AuthResponse {
        val response = AuthResponse(
            UserInfoResponse(
                name = "Mock User",
                locationName = "Mock Location",
            ),
        )
        return response
    }
}