package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.repository.StockRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class RegisterStockUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val stockRepository: StockRepository
) : UseCase<Unit, String>(dispatcher) {

    override suspend fun execute(params: Unit): String {
        return stockRepository.registerStock()
    }
}