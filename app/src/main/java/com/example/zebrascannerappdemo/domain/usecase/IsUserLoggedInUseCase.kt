package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : UseCase<Unit, Boolean>(dispatcher) {
    override suspend fun execute(params: Unit): Boolean {
        val currentUser = userRepository.getCurrentUser().firstOrNull()
        return currentUser != null
    }
}