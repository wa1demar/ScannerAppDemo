package com.example.zebrascannerappdemo.data.network.model.serializer

import com.example.zebrascannerappdemo.domain.enums.ContainerType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = ContainerType::class)
object ContainerTypeSerializer : KSerializer<ContainerType> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ContainerTypeSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ContainerType) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): ContainerType {
        return ContainerType.valueOf(decoder.decodeString())
    }
}