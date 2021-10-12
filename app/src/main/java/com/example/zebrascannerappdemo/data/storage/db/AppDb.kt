package com.example.zebrascannerappdemo.data.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zebrascannerappdemo.data.storage.db.dao.ProductDao
import com.example.zebrascannerappdemo.data.storage.db.dao.StockJobDao
import com.example.zebrascannerappdemo.data.storage.db.entity.JobEntity
import com.example.zebrascannerappdemo.data.storage.db.entity.ProductEntity

@Database(
    entities = [
        JobEntity::class,
        ProductEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class AppDb : RoomDatabase() {

    abstract fun jobDao(): StockJobDao

    abstract fun productDao(): ProductDao
}