package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.scarletstudio.ajudantedecostura.R

/** Reuses the original brand artwork; does not redraw or generate the logo. */
@Composable
fun BrandLogo(modifier: Modifier = Modifier) {
    Image(painterResource(R.drawable.brand_logo), contentDescription = "Logotipo Ajudante de Costura", modifier = modifier)
}

@Composable
fun BrandSplashScreen(onFinished: () -> Unit) {
    val reveal = remember { Animatable(0f) }
    val finish by rememberUpdatedState(onFinished)
    LaunchedEffect(Unit) {
        reveal.animateTo(1f, tween(1100, easing = FastOutSlowInEasing))
        finish()
    }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFF2489E), Color(0xFFDD328F))))) {
        Column(Modifier.align(Alignment.Center).safeDrawingPadding().padding(32.dp)
            .graphicsLayer {
                alpha = .4f + .6f * reveal.value
                scaleX = .93f + .07f * reveal.value
                scaleY = scaleX
                translationY = 16.dp.toPx() * (1f - reveal.value)
            }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(20.dp)) {
            BrandLogo(Modifier.size(160.dp))
            Text("Ajudante\nde Costura", color = Color.White, textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
            Text("Seu talento merece esse apoio.", color = Color.White, textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}
