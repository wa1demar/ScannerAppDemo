package com.example.zebrascannerappdemo.domain.model

import android.os.Parcelable
import com.example.zebrascannerappdemo.domain.enums.JobType
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobInfo(
    val jobType: JobType,
    val reason: String? = null,
    val container: String? = null,
    val products: List<Product>
) : Parcelable