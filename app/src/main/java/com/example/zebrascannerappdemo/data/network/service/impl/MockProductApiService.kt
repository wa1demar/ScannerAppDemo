package com.example.zebrascannerappdemo.data.network.service.impl

import com.example.zebrascannerappdemo.data.network.common.Result
import com.example.zebrascannerappdemo.data.network.model.response.ProductResponse
import com.example.zebrascannerappdemo.data.network.service.ProductApiService
import com.example.zebrascannerappdemo.data.network.service.mock.SearchMock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockProductApiService @Inject constructor(): ProductApiService {
    override suspend fun searchProduct(product: String): Result<ProductResponse> {
        val response = SearchMock.createProductSearchResult()
        return Result.Success.HttpResponse(
            response,
            200,
            null,
            ""
        )
    }
}