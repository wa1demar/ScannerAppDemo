package com.example.zebrascannerappdemo.data.storage.db.dao

import androidx.room.*
import com.example.zebrascannerappdemo.data.storage.db.entity.JobEntity
import com.example.zebrascannerappdemo.data.storage.db.entity.JobWithProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface StockJobDao {

    @Query("UPDATE jobs SET reason = :reason WHERE jobType = :jobType")
    suspend fun updateJobReason(jobType: String, reason: String)

    @Transaction
    @Query("SELECT * FROM jobs WHERE jobType = :type LIMIT 1")
    fun getJobByType(type: String): Flow<JobWithProducts?>

    @Query("SELECT * FROM jobs WHERE jobType = :type")
    suspend fun getSimpleJobByType(type: String): JobEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storeJob(stockJobEntity: JobEntity)

    @Query("DELETE FROM jobs WHERE jobType = :type")
    suspend fun removeJobByType(type: String)
}