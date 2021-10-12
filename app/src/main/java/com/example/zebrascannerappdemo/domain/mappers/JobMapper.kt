package com.example.zebrascannerappdemo.domain.mappers

import com.example.zebrascannerappdemo.data.storage.db.entity.JobEntity
import com.example.zebrascannerappdemo.data.storage.db.entity.JobWithProducts
import com.example.zebrascannerappdemo.domain.model.JobInfo

fun JobWithProducts.toJob(): JobInfo {
    return JobInfo(
        jobType = this.job.jobType,
        reason = this.job.reason,
        container = this.job.container,
        products = this.products.toProductsList(),
    )
}

fun JobInfo.toJobEntity(): JobEntity {
    return JobEntity(
        this.jobType,
        this.reason,
        this.container
    )
}