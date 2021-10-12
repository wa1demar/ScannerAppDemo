package com.example.zebrascannerappdemo.data.storage.prefs

import com.example.zebrascannerappdemo.data.storage.prefs.proto.UserData
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object UserStoreSerializer : Serializer<UserData> {

    override val defaultValue: UserData = UserData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserData {
        try {
            return UserData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserData, output: OutputStream) = t.writeTo(output)
}