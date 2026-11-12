package com.scarletstudio.ajudantedecostura.ui.screens

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.scarletstudio.ajudantedecostura.data.learning.VideoLesson
import com.scarletstudio.ajudantedecostura.data.learning.YouTubeVideo

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouTubeLessonScreen(lesson: VideoLesson, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val videoId = YouTubeVideo.id(lesson.videoUrl)
    val webView = remember(lesson.id, videoId) {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    if (!request.isForMainFrame) return request.url.scheme != "https"
                    if (request.url.scheme == "https") {
                        runCatching { context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, request.url)) }
                    }
                    return true
                }
            }
            if (videoId != null) {
                val origin = "https://${context.packageName}"
                loadDataWithBaseURL(origin, """
                    <!doctype html><html><head><meta name="viewport" content="width=device-width, initial-scale=1">
                    <meta name="referrer" content="strict-origin-when-cross-origin">
                    <style>html,body{margin:0;height:100%;background:#000}iframe{width:100%;height:100%;border:0}</style></head>
                    <body><iframe id="player" title="Aula em vídeo" src="https://www.youtube.com/embed/$videoId?playsinline=1&enablejsapi=1&origin=$origin&fs=0"
                    allow="encrypted-media; picture-in-picture" referrerpolicy="strict-origin-when-cross-origin"></iframe></body></html>
                """.trimIndent(), "text/html", "UTF-8", null)
            }
        }
    }
    DisposableEffect(webView, lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                webView.evaluateJavascript("document.getElementById('player').contentWindow.postMessage(JSON.stringify({event:'command',func:'pauseVideo',args:[]}), 'https://www.youtube.com');", null)
                webView.onPause()
            } else if (event == Lifecycle.Event.ON_RESUME) webView.onResume()
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            webView.stopLoading()
            webView.loadUrl("about:blank")
            webView.destroy()
        }
    }
    Scaffold(topBar = {
        TopAppBar(title = { Text("Assistir aula") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar às aulas") }
        })
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
            InlineAd()
            if (videoId != null) {
                AndroidView(factory = { webView }, modifier = Modifier.fillMaxWidth().heightIn(min = 220.dp).aspectRatio(16f / 9f))
            } else Text("O link desta aula está inválido.", Modifier.padding(16.dp))
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(lesson.title, style = MaterialTheme.typography.headlineSmall)
                Text(lesson.description)
                Text("Vídeo do YouTube. É necessário estar conectado à internet.", style = MaterialTheme.typography.bodySmall)
                TextButton(onClick = { webView.reload() }) { Text("Recarregar vídeo") }
            }
        }
    }
}

