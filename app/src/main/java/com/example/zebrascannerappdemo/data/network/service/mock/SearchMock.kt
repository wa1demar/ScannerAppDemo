package com.example.zebrascannerappdemo.data.network.service.mock

import com.example.zebrascannerappdemo.data.network.model.response.ContainerInfoResponse
import com.example.zebrascannerappdemo.data.network.model.response.ContainerSearchResultResponse
import com.example.zebrascannerappdemo.data.network.model.response.ProductResponse
import com.example.zebrascannerappdemo.domain.enums.ContainerType

object SearchMock {
    fun createContainerSearchResult(): ContainerSearchResultResponse {
        return ContainerSearchResultResponse(
            containerInfo = ContainerInfoResponse(
                barcode = "Z1.A1.A2",
                id = 1,
                containerType = ContainerType.NORMAL_PICKABLE_LOCATION,
                containerLockedForStockTake = false
            )
        )
    }

    fun createProductSearchResult(): ProductResponse {
        return ProductResponse(
            barcode = "Z1.A1.A2",
            productName = "Test Product",
            sku = "12456"
        )
    }
}