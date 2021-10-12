package com.example.zebrascannerappdemo.domain.model

import com.example.zebrascannerappdemo.domain.enums.ContainerType

class Container(
    val barcode: String,
    val containerType: ContainerType,
    val containerLockedForStockTake: Boolean,
)