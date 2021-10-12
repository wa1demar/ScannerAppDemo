package com.example.zebrascannerappdemo.domain.repository

import com.example.zebrascannerappdemo.data.network.common.asFailure
import com.example.zebrascannerappdemo.data.network.common.asSuccess
import com.example.zebrascannerappdemo.data.network.common.isSuccess
import com.example.zebrascannerappdemo.data.network.model.response.ContainerInfoResponse
import com.example.zebrascannerappdemo.data.network.model.response.ContainerSearchResultResponse
import com.example.zebrascannerappdemo.data.network.service.ContainerApiService
import com.example.zebrascannerappdemo.data.storage.db.dao.StockJobDao
import com.example.zebrascannerappdemo.di.Mock
import com.example.zebrascannerappdemo.domain.enums.ContainerType
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.exception.ContainerLockedException
import com.example.zebrascannerappdemo.domain.exception.ContainerNotFoundException
import com.example.zebrascannerappdemo.domain.exception.WrongContainerTypeException
import com.example.zebrascannerappdemo.domain.mappers.toContainer
import com.example.zebrascannerappdemo.domain.mappers.toJobEntity
import com.example.zebrascannerappdemo.domain.model.Container
import com.example.zebrascannerappdemo.domain.model.JobInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContainerRepository @Inject constructor(
    @Mock private val apiService: ContainerApiService,
    private val stockJobDao: StockJobDao,
) {
    suspend fun checkContainer(
        containerBarcode: String,
        jobType: JobType,
        jobReason: String?
    ): Container {
        val response = apiService.searchContainer(containerBarcode)

        if (response.isSuccess()) {
            val resultData = response.asSuccess().value
            val exception = validateContainerForType(jobType, resultData.containerInfo)
            if (exception != null) {
                throw exception
            }
            when (jobType) {
                JobType.REGISTER,
                JobType.MOVE,
                JobType.CHECK,
                JobType.GOODS_IN, // ?
                JobType.REMOVE -> {
                    saveStockJob(
                        resultData,
                        jobType,
                        jobReason,
                    )
                }
                else -> {
                    // do nothing
                }
            }
            return resultData.toContainer()
        } else {
            throw response.asFailure().error!!
        }
    }

    private suspend fun saveStockJob(
        resultData: ContainerSearchResultResponse,
        jobType: JobType,
        jobReason: String?
    ) {
        val containerResult = resultData.toContainer()

        val job = JobInfo(
            jobType = jobType,
            reason = jobReason,
            container = containerResult.barcode,
            listOf()
        )
        stockJobDao.storeJob(job.toJobEntity())
    }

    private fun validateContainerForType(
        jobType: JobType,
        containerInfo: ContainerInfoResponse?
    ): Exception? {
        return when (jobType) {
            JobType.REGISTER -> {
                if (containerInfo == null) {
                    return ContainerNotFoundException()
                }
                if (containerInfo.containerLockedForStockTake) {
                    return ContainerLockedException()
                }
                if (!validTypesForRegister.contains(containerInfo.containerType)) {
                    return WrongContainerTypeException(containerInfo.containerType)
                }
                return null
            }
            else -> {
                return null
            }
        }
    }

    companion object {
        private val validTypesForRegister = listOf(
            ContainerType.GOODS_IN,
            ContainerType.NORMAL_PICKABLE_LOCATION,
            ContainerType.BULK,
            ContainerType.QUARANTINE
        )
    }
}