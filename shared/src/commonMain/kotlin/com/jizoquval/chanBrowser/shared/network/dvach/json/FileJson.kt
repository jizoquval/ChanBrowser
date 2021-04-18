package com.jizoquval.chanBrowser.shared.network.dvach.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileJson(
    @SerialName("path")
    val path: String,
    @SerialName("type")
    val type: Long,
    @SerialName("nsfw")
    val isForAdults: Int = 0,
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int,
    @SerialName("thumbnail")
    val smallFilePath: String?,
    @SerialName("tn_width")
    val smallFWidth: Int?,
    @SerialName("tn_height")
    val smallFHeight: Int?
)
