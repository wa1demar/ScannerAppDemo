package com.example.zebrascannerappdemo.di

import com.example.zebrascannerappdemo.domain.usecase.GetJobByTypeUseCase
import com.example.zebrascannerappdemo.presentation.ui.main.delegates.JobViewModelDelegate
import com.example.zebrascannerappdemo.presentation.ui.main.delegates.JobViewModelDelegateImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@InstallIn(SingletonComponent::class)
@Module
object JobViewModelDelegateModule {

    @Provides
    fun provideJobViewModelDelegate(
        getJobByTypeUseCase: GetJobByTypeUseCase,
        @ApplicationScope externalScope: CoroutineScope
    ): JobViewModelDelegate =
        JobViewModelDelegateImpl(getJobByTypeUseCase, externalScope)
}