package com.jizoquval.chanBrowser.shared.network

import com.jizoquval.chanBrowser.shared.logger.log
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import com.jizoquval.chanBrowser.shared.logger.LogLevel.ERROR

enum class Endpoint(
    val url: String
) {
    DvaCh("2ch.hk"), FourChan("todo")
}

object NetworkException : RuntimeException()

fun createHttpClient(endpoint: Endpoint) = HttpClient {
    install(JsonFeature) {
        val json = kotlinx.serialization.json.Json {
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
        handleResponseException { cause: Throwable ->
            log(ERROR, "client got exception", cause)
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