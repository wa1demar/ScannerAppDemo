package com.example.zebrascannerappdemo.data.storage.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.zebrascannerappdemo.data.storage.db.entity.ProductEntity

@Dao
interface ProductDao {

    @Query(
        """
        SELECT * FROM products
        WHERE barcode = :barcode AND jobType = :jobType
        LIMIT 1
        """
    )
    suspend fun getItemByBarcodeAndJobType(barcode: String, jobType: String): ProductEntity?

    @Query("UPDATE products SET quantity = :quantity WHERE id = :id")
    suspend fun updateItemQuantity(id: Long, quantity: Int)

    @Insert
    suspend fun storeProduct(entity: ProductEntity)

    @Transaction
    suspend fun addProductToJob(entity: ProductEntity) {
        val existedProduct = getItemByBarcodeAndJobType(entity.barcode, entity.jobType.toString())
        if (existedProduct == null) {
            storeProduct(entity.copy(quantity = 1))
        } else {
            updateItemQuantity(existedProduct.id, existedProduct.quantity + 1)
        }
    }

    @Query("DELETE FROM products WHERE jobType = :jobType")
    suspend fun removeProductsByType(jobType: String)

    @Query("DELETE FROM products WHERE barcode = :barcode AND jobType = :jobType")
    suspend fun removeItem(barcode: String, jobType: String)
}