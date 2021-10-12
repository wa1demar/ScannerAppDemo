package com.example.zebrascannerappdemo.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.zebrascannerappdemo.data.storage.prefs.AuthStoreSerializer
import com.example.zebrascannerappdemo.data.storage.prefs.UserStoreSerializer
import com.example.zebrascannerappdemo.data.storage.prefs.proto.UserCredentials
import com.example.zebrascannerappdemo.data.storage.prefs.proto.UserData

private const val USER_DATA_STORE_FILE_NAME = "user_store.pb"
private const val AUTH_DATA_STORE_FILE_NAME = "auth_store.pb"

internal val Context.userDataStore: DataStore<UserData> by dataStore(
    fileName = USER_DATA_STORE_FILE_NAME,
    serializer = UserStoreSerializer
)

internal val Context.authDataStore: DataStore<UserCredentials> by dataStore(
    fileName = AUTH_DATA_STORE_FILE_NAME,
    serializer = AuthStoreSerializer,
)