package com.example.zebrascannerappdemo.domain.repository

import com.example.zebrascannerappdemo.data.network.common.asFailure
import com.example.zebrascannerappdemo.data.network.common.asSuccess
import com.example.zebrascannerappdemo.data.network.common.isSuccess
import com.example.zebrascannerappdemo.data.network.model.response.ProductResponse
import com.example.zebrascannerappdemo.data.network.service.ProductApiService
import com.example.zebrascannerappdemo.data.storage.db.dao.ProductDao
import com.example.zebrascannerappdemo.data.storage.db.dao.StockJobDao
import com.example.zebrascannerappdemo.di.Mock
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.mappers.toJob
import com.example.zebrascannerappdemo.domain.mappers.toJobEntity
import com.example.zebrascannerappdemo.domain.mappers.toProduct
import com.example.zebrascannerappdemo.domain.mappers.toProductEntity
import com.example.zebrascannerappdemo.domain.model.JobInfo
import com.example.zebrascannerappdemo.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobRepository @Inject constructor(
    private val stockJobDao: StockJobDao,
    private val productDao: ProductDao,
    @Mock private val apiService: ProductApiService,
) {

    fun getJobByType(jobType: JobType): Flow<JobInfo?> {
        return stockJobDao.getJobByType(jobType.toString())
            .map { it?.toJob() }
    }

    suspend fun updateJobReason(
        jobType: JobType,
        reason: String,
    ) {
        stockJobDao.updateJobReason(jobType.toString(), reason)
    }

    suspend fun addProductToJob(barcode: String, jobType: JobType): Product {
        val currentItem = productDao.getItemByBarcodeAndJobType(barcode, jobType.toString())
        if (currentItem == null) {
            val response = apiService.searchProduct(
                barcode
            )
            if (response.isSuccess()) {
                val productResponse = response.asSuccess().value
                addItemToJob(productResponse, jobType)
                return productResponse.toProduct()
            } else {
                throw response.asFailure().error!!
            }
        } else {
            productDao.updateItemQuantity(currentItem.id, currentItem.quantity + 1)
            return currentItem.toProduct()
        }
    }

    private suspend fun addItemToJob(product: ProductResponse, jobType: JobType) {
        productDao.addProductToJob(product.toProductEntity(jobType))
    }

    suspend fun clearJob(jobType: JobType) {
        productDao.removeProductsByType(jobType.toString())
        stockJobDao.removeJobByType(jobType.toString())
    }

    suspend fun removeProductFromJob(barcode: String, jobType: JobType) {
        val currentItem = productDao.getItemByBarcodeAndJobType(barcode, jobType.toString())
            ?: return
        if (currentItem.quantity > 1) {
            productDao.updateItemQuantity(currentItem.id, currentItem.quantity - 1)
        } else {
            productDao.removeItem(barcode, jobType.toString())
        }
    }

    suspend fun removeAllItemsFromJob(barcode: String, jobType: JobType) {
        productDao.removeItem(barcode, jobType.toString())
    }
}