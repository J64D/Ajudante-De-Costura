package com.scarletstudio.ajudantedecostura.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.material.icons.rounded.ContentCut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/** A vector scissors illustration with a brief, gentle entrance. */
@Composable
fun LiquidHero(eyebrow: String, title: String, subtitle: String, modifier: Modifier = Modifier) {
    val rise = remember { Animatable(0f) }
    LaunchedEffect(Unit) { rise.animateTo(1f, spring(dampingRatio = .72f, stiffness = 38f)) }
    Box(modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White)) {
        Icon(
            imageVector = Icons.Rounded.ContentCut,
            contentDescription = null,
            tint = Rose.copy(alpha = .62f),
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 18.dp).size(72.dp)
                .graphicsLayer {
                    val progress = rise.value.coerceIn(0f, 1f)
                    alpha = progress
                    translationY = 20.dp.toPx() * (1f - progress)
                    rotationZ = -28f + 16f * progress
                    scaleX = .88f + .12f * progress
                    scaleY = scaleX
                }
        )
        Column(Modifier.padding(start = 24.dp, top = 24.dp, end = 104.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.headlineMedium, color = RoseInk)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = RoseMuted)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun BubbleIcon(icon: ImageVector, modifier: Modifier = Modifier) {
    Surface(modifier.size(48.dp), shape = CircleShape, color = Petal, border = BorderStroke(1.dp, Color.White)) {
        Box(contentAlignment = Alignment.Center) { Icon(icon, null, tint = Rose, modifier = Modifier.size(24.dp)) }
    }
}

@Composable
fun AtelierAction(title: String, subtitle: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) .97f else 1f, spring(dampingRatio = .65f, stiffness = 450f), label = "Toque suave")
    Card(onClick = onClick, interactionSource = interaction,
        modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Petal), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            BubbleIcon(icon)
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = RoseMuted)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtelierTopBar(title: String, onBack: () -> Unit, actions: @Composable RowScope.() -> Unit = {}) {
    TopAppBar(title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Voltar") } },
        actions = actions, colors = TopAppBarDefaults.topAppBarColors(containerColor = Blush))
}
