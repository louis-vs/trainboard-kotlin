package com.softwire.lner.trainboard.mobile.models

import com.softwire.lner.trainboard.mobile.util.Color
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor

@Serializable(with = StatusSerializer::class)
enum class Status (val statusText: String){
    NORMAL("normal"),
    DELAYED("delayed"),
    CANCELLED("cancelled"),
    FULLY_RESERVED("full");

    val backgroundColor: Color
        get() = when (this) {
            NORMAL -> Color(0xff, 0x00, 0x88, 0x00)
            DELAYED -> Color(0xff, 0xff, 0xbb, 0x00)
            CANCELLED -> Color(0xff, 0xff, 0x00, 0x00)
            FULLY_RESERVED -> Color(0xff, 0x00, 0x00, 0x00)
        }

    val textColor: Color
        get() = when (this) {
            DELAYED -> Color(0xff, 0x00, 0x00, 0x00)
            else -> Color(0xff, 0xff, 0xff, 0xff)
        }
}

@Serializer(forClass = Status::class)
object StatusSerializer {
    override val descriptor: SerialDescriptor = StringDescriptor

    override fun deserialize(decoder: Decoder): Status {
        return Status.valueOf(decoder.decodeString().toUpperCase())
    }

    override fun serialize(encoder: Encoder, status: Status) {
        encoder.encodeString(status.name.toLowerCase())
    }
}
