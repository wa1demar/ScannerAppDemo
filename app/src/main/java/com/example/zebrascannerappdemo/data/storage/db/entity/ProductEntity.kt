package com.example.zebrascannerappdemo.data.storage.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.zebrascannerappdemo.data.storage.db.converter.JobTypeConverter
import com.example.zebrascannerappdemo.domain.enums.JobType

@Entity(tableName = "products")
@TypeConverters(JobTypeConverter::class)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val barcode: String,
    val productName: String,
    val jobType: JobType,
    val quantity: Int = 1
)