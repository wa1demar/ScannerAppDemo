package com.example.zebrascannerappdemo.data.network.service.impl

import com.example.zebrascannerappdemo.data.network.common.Result
import com.example.zebrascannerappdemo.data.network.model.request.RegisterStockRequest
import com.example.zebrascannerappdemo.data.network.model.response.EmptyResponse
import com.example.zebrascannerappdemo.data.network.service.StockApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockStockApiService @Inject constructor() : StockApiService {
    override suspend fun registerStock(stockRequest: RegisterStockRequest): Result<EmptyResponse> {
        val response = EmptyResponse("Registered")
        return Result.Success.HttpResponse(
            response,
            200,
            null,
            ""
        )
    }
}