package com.example.zebrascannerappdemo.domain.mappers

import com.example.zebrascannerappdemo.data.network.model.response.ContainerSearchResultResponse
import com.example.zebrascannerappdemo.domain.model.Container

fun ContainerSearchResultResponse.toContainer(): Container {
    checkNotNull(this.containerInfo)
    return Container(
        this.containerInfo.barcode,
        this.containerInfo.containerType,
        this.containerInfo.containerLockedForStockTake
    )
}