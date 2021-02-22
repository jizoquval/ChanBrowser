package com.ninjaval.dvachBrowser.shared.network

import com.ninjaval.dvachBrowser.shared.network.pojo.BoardJson
import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

class DvachApi {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json {
                ignoreUnknownKeys = true
            }
            serializer = KotlinxSerializer(json)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    suspend fun getBoards(): Map<String, List<BoardJson>> {
        return httpClient.get(DVA_4_ENDPOINT)
    }

    fun getThreads() {

    }

    fun getPosts() {

    }

    companion object {
        private const val DVA_4_ENDPOINT = "https://2ch.hk/makaba/mobile.fcgi?task=get_boards"
    }
}