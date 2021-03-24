package com.jizoquval.chanBrowser.shared.network

import com.jizoquval.chanBrowser.shared.network.json.BoardJson
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

private const val DVA_4_ENDPOINT = "2ch.hk"
private const val DVA_4_PATH_MOBILE = "makaba/mobile.fcgi"

internal class DvachApi : IDvachApi {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json {
                ignoreUnknownKeys = true
            }
            serializer = KotlinxSerializer(json)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status.value
                when (statusCode) {
                    in 300..399 -> throw RedirectResponseException(
                        response,
                        "Get redirect exception $statusCode"
                    )
                    in 400..499 -> throw ClientRequestException(
                        response,
                        "Get client exception $statusCode"
                    )
                    in 500..599 -> throw ServerResponseException(
                        response,
                        "Get server exception $statusCode"
                    )
                }

                if (statusCode >= 600) {
                    throw ResponseException(response, "Get http exception $statusCode")
                }
            }
        }
        defaultRequest {
            url {
                host = DVA_4_ENDPOINT
                protocol = URLProtocol.HTTPS
            }
        }
    }

    override suspend fun getBoards(): Map<String, List<BoardJson>> =
        httpClient.get {
            url {
                encodedPath = DVA_4_PATH_MOBILE
                parameter("task", "get_boards")
            }
        }

    override suspend fun getThreads(boardName: String): String = httpClient.get {
        url {
            encodedPath = "$boardName/catalog.json"
        }
    }

    internal fun getPosts() {

    }
}