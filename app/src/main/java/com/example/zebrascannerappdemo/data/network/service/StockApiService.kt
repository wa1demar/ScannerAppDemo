package com.example.zebrascannerappdemo.data.network.service

import com.example.zebrascannerappdemo.data.network.model.request.RegisterStockRequest
import com.example.zebrascannerappdemo.data.network.model.response.EmptyResponse
import com.example.zebrascannerappdemo.data.network.common.Result
import retrofit2.http.Body
import retrofit2.http.POST

interface StockApiService {

    @POST("product/register")
    suspend fun registerStock(@Body stockRequest: RegisterStockRequest): Result<EmptyResponse>
}