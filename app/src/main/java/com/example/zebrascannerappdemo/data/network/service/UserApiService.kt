package com.example.zebrascannerappdemo.data.network.service

import com.example.zebrascannerappdemo.data.network.model.response.LogoutResponse
import com.example.zebrascannerappdemo.data.network.common.Result
import retrofit2.http.POST

interface UserApiService {

    @POST("misc/logout")
    suspend fun logout(): Result<LogoutResponse>
}