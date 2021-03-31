package com.jizoquval.chanBrowser.shared.network.dvach

import com.jizoquval.chanBrowser.shared.network.json.BoardJson
import com.jizoquval.chanBrowser.shared.network.json.ThreadsListJson
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val DVA_4_PATH_MOBILE = "makaba/mobile.fcgi"

internal class DvachApi(
    private val httpClient: HttpClient,
    private val backgroundDispatcher: CoroutineDispatcher
) : IDvachApi {

    override suspend fun getBoards(): Map<String, List<BoardJson>> =
        withContext(backgroundDispatcher) {
            httpClient.get {
                url {
                    encodedPath = DVA_4_PATH_MOBILE
                    parameter("task", "get_boards")
                }
            }
        }

    override suspend fun getThreads(boardName: String): ThreadsListJson =
        withContext(backgroundDispatcher) {
            httpClient.get {
                url {
                    encodedPath = "$boardName/catalog.json"
                }
            }
        }

    internal fun getPosts() {
    }
}
