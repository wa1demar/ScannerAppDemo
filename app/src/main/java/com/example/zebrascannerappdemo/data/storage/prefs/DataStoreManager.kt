package com.example.zebrascannerappdemo.data.storage.prefs

import android.content.Context
import com.example.zebrascannerappdemo.data.storage.authDataStore
import com.example.zebrascannerappdemo.data.storage.prefs.model.CredentialsDto
import com.example.zebrascannerappdemo.data.storage.prefs.model.UserDto
import com.example.zebrascannerappdemo.data.storage.prefs.proto.UserCredentials
import com.example.zebrascannerappdemo.data.storage.prefs.proto.UserData
import com.example.zebrascannerappdemo.data.storage.userDataStore
import com.example.zebrascannerappdemo.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun storeCurrentUser(user: UserDto) {
        context.userDataStore.updateData { store ->
            store.toBuilder()
                .setName(user.name)
                .setLocationName(user.locationName)
                .build()
        }
    }

    fun getCurrentUser(): Flow<UserDto?> {
        return context.userDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Timber.d("getCurrentUser() $exception")
                    emit(UserData.getDefaultInstance())
                } else {
                    Timber.d("getCurrentUser() $exception")
                    throw exception
                }
            }.map { data ->
                if (data.name.isNullOrEmpty() && data.locationName.isNullOrEmpty()) {
                    null
                } else {
                    UserDto(
                        data.name,
                        data.locationName,
                    )
                }
            }
    }

    suspend fun storeCredentials(credentials: CredentialsDto) {
        context.authDataStore.updateData { store ->
            store.toBuilder()
                .setUsername(credentials.username)
                .setPassword(credentials.password)
                .build()
        }
    }

    fun getCredentials(): Flow<CredentialsDto?> {
        return context.authDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Timber.d("getCurrentUser() $exception")
                    emit(UserCredentials.getDefaultInstance())
                } else {
                    Timber.d("getCurrentUser() $exception")
                    throw exception
                }
            }
            .map { data ->
                if (data.username.isNullOrEmpty() || data.password.isNullOrEmpty()) {
                    null
                } else {
                    CredentialsDto(
                        data.username,
                        data.password
                    )
                }
            }
    }

    fun clearAllSync() {
        val scope = CoroutineScope(dispatcher + Job())
        scope.launch {
            clearCurrentUser()
            clearCredentials()
        }
    }

    suspend fun clearCurrentUser() {
        context.userDataStore.updateData { protoBuilder ->
            protoBuilder.toBuilder()
                .clear()
                .build()
        }
    }

    suspend fun clearCredentials() {
        context.authDataStore.updateData { protoBuilder ->
            protoBuilder.toBuilder()
                .clear()
                .build()
        }
    }
}