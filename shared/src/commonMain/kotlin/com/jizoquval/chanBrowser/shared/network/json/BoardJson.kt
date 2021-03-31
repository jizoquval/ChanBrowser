package com.jizoquval.chanBrowser.shared.network.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoardJson(
    @SerialName("bump_limit")
    val bumpLimit: Int,
    @SerialName("category")
    val category: String,
    @SerialName("default_name")
    val defaultName: String,
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("pages")
    val pages: Int,
    @SerialName("sage")
    val sage: Int,
    @SerialName("tripcodes")
    val tripCodes: Int,
    @SerialName("enable_dices")
    val enableDices: Int,
    @SerialName("enable_flags")
    val enableFlags: Int,
    @SerialName("enable_likes")
    val enableLikes: Int,
    @SerialName("enable_icons")
    val enableIcons: Int,
    @SerialName("enable_names")
    val enableNames: Int,
    @SerialName("enable_oekaki")
    val enableOekaki: Int,
    @SerialName("enable_posting")
    val enablePosting: Int,
    @SerialName("enable_sage")
    val enableSage: Int,
    @SerialName("enable_shield")
    val enableShield: Int,
    @SerialName("enable_subject")
    val enableSubject: Int,
    @SerialName("enable_thread_tags")
    val enableThreadTags: Int,
    @SerialName("enable_trips")
    val enableTrips: Int,
    @SerialName("icons")
    val icons: List<Icon>?,
)
