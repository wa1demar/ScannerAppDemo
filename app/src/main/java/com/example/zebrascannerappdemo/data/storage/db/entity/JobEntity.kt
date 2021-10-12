package com.example.zebrascannerappdemo.data.storage.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.zebrascannerappdemo.domain.enums.JobType

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey val jobType: JobType,
    val reason: String?,
    val container: String?,
    )