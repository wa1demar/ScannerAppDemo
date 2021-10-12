package com.example.zebrascannerappdemo.domain.repository

import com.example.zebrascannerappdemo.data.network.common.asFailure
import com.example.zebrascannerappdemo.data.network.common.asSuccess
import com.example.zebrascannerappdemo.data.network.common.isSuccess
import com.example.zebrascannerappdemo.data.network.model.request.RegisterStockRequest
import com.example.zebrascannerappdemo.data.network.service.StockApiService
import com.example.zebrascannerappdemo.data.storage.db.dao.StockJobDao
import com.example.zebrascannerappdemo.data.storage.db.entity.ProductEntity
import com.example.zebrascannerappdemo.di.Mock
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.exception.JobNotStartedException
import com.example.zebrascannerappdemo.domain.exception.ValidationException
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepository @Inject constructor(
    private val jobDao: StockJobDao,
    @Mock private val apiService: StockApiService,
    private val jobRepository: JobRepository,
){
    suspend fun registerStock(): String {
        val jobWithProducts =
            jobDao.getJobByType(JobType.REGISTER.toString())
                .firstOrNull() ?: throw JobNotStartedException()

        if (jobWithProducts.job.reason == null) {
            throw ValidationException()
        }

        val response = apiService.registerStock(
            RegisterStockRequest(
                requireNotNull(jobWithProducts.job.container),
                requireNotNull(jobWithProducts.job.reason),
                jobWithProducts.products.toMap()
            )
        )

        if (response.isSuccess()) {
            jobRepository.clearJob(JobType.REGISTER)
            return response.asSuccess().value.message!!
        } else {
            throw response.asFailure().error!!
        }
    }
}

fun List<ProductEntity>.toMap(): Map<String, Int> {
    return this.map { it.barcode to it.quantity }.toMap()
}