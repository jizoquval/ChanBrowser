package com.jizoquval.chanBrowser.shared.network.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileJson(
    @SerialName("path")
    val path: String,
    @SerialName("type")
    val type: Long
)
