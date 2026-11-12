package com.scarletstudio.ajudantedecostura.data.learning

import java.net.URI
import java.net.URLDecoder

object YouTubeVideo {
    fun id(input: String): String? = runCatching {
        val uri = URI(input.trim())
        if (uri.scheme !in listOf("https", "http")) return null
        val path = uri.path.orEmpty().split('/').filter { it.isNotEmpty() }
        val value = when (uri.host?.lowercase()) {
            "youtu.be" -> path.singleOrNull()
            "youtube.com", "www.youtube.com", "m.youtube.com", "www.youtube-nocookie.com" -> {
                if (path.firstOrNull() in listOf("embed", "shorts", "live") && path.size == 2) path[1]
                else if (uri.path == "/watch") uri.rawQuery.orEmpty().split('&')
                    .map { it.split('=', limit = 2) }
                    .firstOrNull { it.size == 2 && it[0] == "v" }
                    ?.get(1)?.let { URLDecoder.decode(it, "UTF-8") }
                else null
            }
            else -> null
        }
        value?.takeIf { it.matches(Regex("[A-Za-z0-9_-]{11}")) }
    }.getOrNull()
}
