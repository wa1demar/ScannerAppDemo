package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.model.Resource
import com.example.zebrascannerappdemo.domain.model.User
import com.example.zebrascannerappdemo.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : FlowUseCase<Unit, User?>(dispatcher) {
    override fun execute(params: Unit): Flow<Resource<User?>> {
        return userRepository.getCurrentUser()
            .catch { Resource.Success(false) }
            .map { Resource.Success(it) }
    }
}