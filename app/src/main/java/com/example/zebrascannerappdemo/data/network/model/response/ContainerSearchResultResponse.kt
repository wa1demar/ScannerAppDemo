package com.example.zebrascannerappdemo.data.network.model.response

import com.example.zebrascannerappdemo.data.network.model.serializer.ContainerTypeSerializer
import com.example.zebrascannerappdemo.domain.enums.ContainerType
import kotlinx.serialization.Serializable

@Serializable
data class ContainerSearchResultResponse(
    val containerInfo: ContainerInfoResponse? = null,
)

@Serializable
data class ContainerInfoResponse(
    val barcode: String,
    val id: Long,
    @Serializable(with = ContainerTypeSerializer::class)
    val containerType: ContainerType,
    val containerLockedForStockTake: Boolean,
)