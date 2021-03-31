package com.jizoquval.chanBrowser.shared.network.json

import kotlinx.serialization.Serializable

@Serializable
data class Icon(
    val name: String,
    val num: Int,
    val url: String
)
