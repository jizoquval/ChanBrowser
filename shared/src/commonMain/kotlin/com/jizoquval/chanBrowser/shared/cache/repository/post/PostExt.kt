package com.jizoquval.chanBrowser.shared.cache.repository.post

import com.jizoquval.chanBrowser.shared.cache.Post
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.network.json.ThreadJson
import com.jizoquval.chanBrowser.shared.utils.toBoolean

fun postFromThreadJson(
    chan: Chan,
    json: ThreadJson
) = Post(
        id = json.id,
        threadId = json.id,
        chan = chan,
        title = json.subject,
        message = json.comment,
        date = json.date,
        timestamp = json.timestamp,
        isOP = json.op.toBoolean(),
        isStiky = json.isSticky,
        name = json.authorName,
        email = json.authorEmail,
        likes = json.likes,
        dislikes = json.dislikes
    )