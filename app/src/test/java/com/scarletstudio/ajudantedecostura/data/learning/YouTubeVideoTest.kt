package com.scarletstudio.ajudantedecostura.data.learning

import org.junit.Assert.*
import org.junit.Test

class YouTubeVideoTest {
    @Test fun acceptsSupportedShareLinks() {
        listOf(
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ&t=12",
            "https://youtu.be/dQw4w9WgXcQ?si=abc",
            "https://m.youtube.com/watch?feature=shared&v=dQw4w9WgXcQ",
            "https://youtube.com/shorts/dQw4w9WgXcQ",
            "https://youtube.com/live/dQw4w9WgXcQ",
            "https://www.youtube-nocookie.com/embed/dQw4w9WgXcQ"
        ).forEach { assertEquals(it, "dQw4w9WgXcQ", YouTubeVideo.id(it)) }
    }

    @Test fun rejectsForeignHostsAndInjection() {
        listOf("", "javascript:alert(1)", "https://youtube.com.evil.test/watch?v=dQw4w9WgXcQ",
            "https://youtube.com@evil.test/watch?v=dQw4w9WgXcQ", "https://youtu.be/short",
            "https://www.youtube.com/watch?v=%22%3E%3Cscript%3E", "https://youtube.com/playlist?list=abc",
            "https://youtu.be/dQw4w9WgXcQ/extra"
        ).forEach { assertNull(it, YouTubeVideo.id(it)) }
    }
}
