package com.example.zebrascannerappdemo.domain.repository

import com.example.zebrascannerappdemo.data.network.common.asFailure
import com.example.zebrascannerappdemo.data.network.common.asSuccess
import com.example.zebrascannerappdemo.data.network.common.isSuccess
import com.example.zebrascannerappdemo.data.network.model.request.SignIntRequest
import com.example.zebrascannerappdemo.data.network.model.response.UserInfoResponse
import com.example.zebrascannerappdemo.data.network.service.ApiService
import com.example.zebrascannerappdemo.data.network.service.UserApiService
import com.example.zebrascannerappdemo.data.storage.prefs.DataStoreManager
import com.example.zebrascannerappdemo.data.storage.prefs.model.CredentialsDto
import com.example.zebrascannerappdemo.di.Mock
import com.example.zebrascannerappdemo.domain.mappers.toUser
import com.example.zebrascannerappdemo.domain.mappers.toUserDto
import com.example.zebrascannerappdemo.domain.model.User
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    @Mock private val apiService: ApiService,
    @Mock private val userApiService: UserApiService,
) {
    fun getCurrentUser(): Flow<User?> {
        return dataStoreManager.getCurrentUser()
            .map {
                Timber.d("getCurrentUser(): $it")
                it
            }
            .map { it?.toUser() }
    }

    suspend fun login(username: String, password: String): Boolean {
        val response = apiService.login(SignIntRequest(username, password))
        if (response.isSuccess()) {
            val authResponse = response.asSuccess().value
            coroutineScope {
                storeUser(authResponse.userInfo)
                storeCredentials(username, password)
            }
            return true
        } else {
            throw response.asFailure().error!!
        }
    }

    private suspend fun storeUser(userInfoResponse: UserInfoResponse) {
        dataStoreManager.storeCurrentUser(userInfoResponse.toUserDto())
    }

    private suspend fun storeCredentials(username: String, password: String) {
        dataStoreManager.storeCredentials(CredentialsDto(username, password))
    }

    suspend fun logout() {
        val response = userApiService.logout()

        if (response.isSuccess()) {
            val logoutResponse = response.asSuccess().value
            Timber.d(logoutResponse.message)
        } else {
            Timber.d("Logout error: ${response.asFailure().error}")
        }
        dataStoreManager.clearCredentials()
        dataStoreManager.clearCurrentUser()
    }
}