package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.*
import com.scarletstudio.ajudantedecostura.BuildConfig
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun AdMobBanner(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val orientation = LocalConfiguration.current.orientation
    BoxWithConstraints(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        val adWidth = maxWidth.value.toInt().coerceAtLeast(1)
        val adSize = remember(context, adWidth, orientation) {
            AdSize.getLargeAnchoredAdaptiveBannerAdSize(context, adWidth)
        }
        key(adWidth, orientation) {
            val ad = remember(context) { AdView(context).apply {
                setAdSize(adSize)
                adUnitId = if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/6300978111" else "ca-app-pub-1367515717522293/6625687240"
            } }
            DisposableEffect(ad, lifecycle) {
                ad.loadAd(AdRequest.Builder().build())
                if (!lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) ad.pause()
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_PAUSE) ad.pause()
                    if (event == Lifecycle.Event.ON_RESUME) ad.resume()
                }
                lifecycle.addObserver(observer)
                onDispose { lifecycle.removeObserver(observer); ad.destroy() }
            }
            AndroidView(factory = { ad }, modifier = Modifier.width(adWidth.dp).height(adSize.height.dp))
        }
    }
}

/** Reserved area measured by Scaffold, separate from content and system navigation. */
@Composable
fun BannerFooter(includeBottomInset: Boolean = true) {
    val density = LocalDensity.current
    if (WindowInsets.ime.getBottom(density) > 0) return
    val sides = if (includeBottomInset) WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom else WindowInsetsSides.Horizontal
    Surface(color = androidx.compose.ui.graphics.Color.White) {
        Column(Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing.only(sides)),
            horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Text("Publicidade", style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
            AdMobBanner()
            Spacer(Modifier.height(8.dp))
        }
    }
}

/** An ad inside the scrolling content, never pinned to the navigation bar. */
@Composable
fun InlineAd() {
    Surface(color = androidx.compose.ui.graphics.Color.White, shape = MaterialTheme.shapes.small) {
        Column(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Publicidade", style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
            AdMobBanner()
        }
    }
}
