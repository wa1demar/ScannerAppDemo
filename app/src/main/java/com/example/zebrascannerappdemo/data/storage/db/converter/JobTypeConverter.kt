package com.example.zebrascannerappdemo.data.storage.db.converter

import androidx.room.TypeConverter
import com.example.zebrascannerappdemo.domain.enums.JobType

class JobTypeConverter {

    @TypeConverter
    fun jobTypeEnumToString(jobType: JobType): String {
        return jobType.toString()
    }

    @TypeConverter
    fun stringToJobTypeEnum(type: String): JobType {
        return JobType.valueOf(type)
    }
}