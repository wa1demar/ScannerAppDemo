package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : UseCase<LoginUseCaseParams, Boolean>(dispatcher) {

    override suspend fun execute(params: LoginUseCaseParams): Boolean {
        return userRepository.login(params.username, params.password)
    }
}

data class LoginUseCaseParams(
    val username: String,
    val password: String,
)