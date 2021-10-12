package com.example.zebrascannerappdemo.data.network.service.impl

import com.example.zebrascannerappdemo.data.network.common.Result
import com.example.zebrascannerappdemo.data.network.model.response.LogoutResponse
import com.example.zebrascannerappdemo.data.network.service.UserApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockUserApiService @Inject constructor(): UserApiService {
    override suspend fun logout(): Result<LogoutResponse> {
        val response = LogoutResponse(
            message = "Success"
        )
        return Result.Success.HttpResponse(
            response,
            200,
            "Successfully logged out",
            "logout"
        )
    }
}