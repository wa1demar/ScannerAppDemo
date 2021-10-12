package com.example.zebrascannerappdemo.data.network.service.impl

import com.example.zebrascannerappdemo.data.network.common.Result
import com.example.zebrascannerappdemo.data.network.model.response.ContainerSearchResultResponse
import com.example.zebrascannerappdemo.data.network.service.ContainerApiService
import com.example.zebrascannerappdemo.data.network.service.mock.SearchMock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockContainerApiService @Inject constructor(): ContainerApiService {

    override suspend fun searchContainer(
        keyword: String,
    ): Result<ContainerSearchResultResponse> {
        val response = SearchMock.createContainerSearchResult()
        return Result.Success.HttpResponse(
            response,
            200,
            null,
            ""
        )
    }
}