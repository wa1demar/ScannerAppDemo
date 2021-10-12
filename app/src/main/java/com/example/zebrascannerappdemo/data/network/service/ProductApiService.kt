package com.example.zebrascannerappdemo.data.network.service

import com.example.zebrascannerappdemo.data.network.model.response.ProductResponse
import com.example.zebrascannerappdemo.data.network.common.Result
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductApiService {
    @POST("lookup/product/{barcode}")
    suspend fun searchProduct(
        @Path("barcode") product: String,
    ): Result<ProductResponse>
}