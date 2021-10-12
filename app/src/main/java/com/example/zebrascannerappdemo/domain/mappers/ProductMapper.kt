package com.example.zebrascannerappdemo.domain.mappers

import com.example.zebrascannerappdemo.data.network.model.response.ProductResponse
import com.example.zebrascannerappdemo.data.storage.db.entity.ProductEntity
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.model.Product

fun List<ProductEntity>.toProductsList(): List<Product> {
    return this.map { it.toProduct() }
}

fun ProductEntity.toProduct(): Product {
    return Product(
        barcode = this.barcode,
        productName = this.productName,
        quantity = this.quantity
    )
}

fun ProductResponse.toProduct(): Product {
    return Product(
        barcode = this.barcode,
        productName = this.productName,
    )
}

fun ProductResponse.toProductEntity(jobType: JobType): ProductEntity {
    return ProductEntity(
        barcode = this.barcode,
        productName = this.productName,
        jobType = jobType,
    )
}