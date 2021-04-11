package com.jizoquval.chanBrowser.shared.network

import co.touchlab.kermit.Kermit
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.http.URLProtocol

enum class Endpoint(
    val url: String
) {
    DvaCh("2ch.hk"), FourChan("todo")
}

object NetworkException : RuntimeException()

fun createHttpClient(
    endpoint: Endpoint,
    log: Kermit
) = HttpClient {
    install(JsonFeature) {
        val json = kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
        }
        serializer = KotlinxSerializer(json)
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                log.i { message }
            }
        }

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
        handleResponseException { cause: Throwable ->
            log.e(cause) { "client got exception" }
            throw NetworkException
        }
    }
    defaultRequest {
        url {
            host = endpoint.url
            protocol = URLProtocol.HTTPS
        }
    }
}
