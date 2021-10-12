package com.example.zebrascannerappdemo.data.storage.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class JobWithProducts(

    @Embedded
    val job: JobEntity,

    @Relation(
        parentColumn = "jobType",
        entityColumn = "jobType"
    )
    val products: List<ProductEntity> = emptyList(),
)