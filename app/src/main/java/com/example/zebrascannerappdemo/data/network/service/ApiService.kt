package com.example.zebrascannerappdemo.data.network.service

import com.example.zebrascannerappdemo.data.network.model.request.SignIntRequest
import com.example.zebrascannerappdemo.data.network.model.response.AuthResponse
import com.example.zebrascannerappdemo.data.network.common.Result
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("misc/login")
    suspend fun login(@Body signIntRequest: SignIntRequest): Result<AuthResponse>
}