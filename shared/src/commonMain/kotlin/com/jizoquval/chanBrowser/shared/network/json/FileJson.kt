package com.jizoquval.chanBrowser.shared.network.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class FileJson(
    @SerialName("path")
    val path: String,
    @SerialName("type")
    val type: Long
)
