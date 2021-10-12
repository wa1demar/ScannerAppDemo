package com.example.zebrascannerappdemo.data.network.service

import com.example.zebrascannerappdemo.data.network.model.response.ContainerSearchResultResponse
import com.example.zebrascannerappdemo.data.network.common.Result
import retrofit2.http.POST
import retrofit2.http.Path

interface ContainerApiService {

    @POST("search/container/{keyword}")
    suspend fun searchContainer(
        @Path("keyword") keyword: String,
    ): Result<ContainerSearchResultResponse>
}