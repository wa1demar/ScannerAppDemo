package com.example.zebrascannerappdemo.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.zebrascannerappdemo.data.storage.db.AppDb
import com.example.zebrascannerappdemo.data.storage.db.dao.ProductDao
import com.example.zebrascannerappdemo.data.storage.db.dao.StockJobDao
import com.example.zebrascannerappdemo.data.storage.prefs.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    private const val DATABASE_NAME = "cache.db"

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application,
        dataStoreManager: DataStoreManager
    ): AppDb {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDb::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                    dataStoreManager.clearAllSync()
                }
            }).build()
    }

    @Provides
    @Singleton
    fun provideStockJobDao(db: AppDb): StockJobDao = db.jobDao()

    @Provides
    @Singleton
    fun provideProductDao(db: AppDb): ProductDao = db.productDao()
}