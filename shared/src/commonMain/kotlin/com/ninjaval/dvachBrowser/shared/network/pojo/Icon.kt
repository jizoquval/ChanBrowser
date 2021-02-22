package com.ninjaval.dvachBrowser.shared.network.pojo

import kotlinx.serialization.Serializable

@Serializable
data class Icon(
    val name: String,
    val num: Int,
    val url: String
)