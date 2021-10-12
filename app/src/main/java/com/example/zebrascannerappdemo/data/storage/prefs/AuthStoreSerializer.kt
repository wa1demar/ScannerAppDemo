package com.example.zebrascannerappdemo.data.storage.prefs

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.zebrascannerappdemo.data.storage.prefs.proto.UserCredentials
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object AuthStoreSerializer : Serializer<UserCredentials> {

    override val defaultValue: UserCredentials = UserCredentials.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserCredentials {
        try {
            return UserCredentials.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserCredentials, output: OutputStream) = t.writeTo(output)
}