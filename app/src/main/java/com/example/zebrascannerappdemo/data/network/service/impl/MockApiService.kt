package com.example.zebrascannerappdemo.data.network.service.impl

import com.example.zebrascannerappdemo.data.network.common.BadRequestException
import com.example.zebrascannerappdemo.data.network.common.Result
import com.example.zebrascannerappdemo.data.network.model.request.SignIntRequest
import com.example.zebrascannerappdemo.data.network.model.response.AuthResponse
import com.example.zebrascannerappdemo.data.network.service.ApiService
import com.example.zebrascannerappdemo.data.network.service.mock.UserMock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockApiService @Inject constructor(): ApiService{

    override suspend fun login(signIntRequest: SignIntRequest): Result<AuthResponse> {
        return if (signIntRequest.authentication.username == MOCK_AUTH_USERNAME &&
            signIntRequest.authentication.password == MOCK_AUTH_PASSWORD
        ) {
            val response = UserMock.create()
            Result.Success.HttpResponse(
                response,
                200,
                "Successfully logged out",
                "login"
            )
        } else {
            Result.Failure.BadRequestError(
                BadRequestException(
                    statusCode = 101,
                    statusMessage = "Authentication failed",
                    url = "login",
                )
            )
        }
    }

    companion object {
        private const val MOCK_AUTH_USERNAME = "Test"
        private const val MOCK_AUTH_PASSWORD = "Test"
    }
}