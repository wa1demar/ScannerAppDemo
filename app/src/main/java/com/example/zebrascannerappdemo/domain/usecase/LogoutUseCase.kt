package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun execute(params: Unit) {
        return userRepository.logout()
    }
}