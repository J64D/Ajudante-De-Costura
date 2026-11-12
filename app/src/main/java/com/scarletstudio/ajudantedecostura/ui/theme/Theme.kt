package com.scarletstudio.ajudantedecostura.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val Rose = Color(0xFFAF245D)
val RoseInk = Color(0xFF542039)
val RoseMuted = Color(0xFF86586C)
val Blush = Color(0xFFFFF4F8)
val Petal = Color(0xFFFFE3ED)
private val AtelierColors = lightColorScheme(
    primary = Rose, onPrimary = Color.White, primaryContainer = Petal, onPrimaryContainer = RoseInk,
    secondary = Color(0xFFC14576), onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFEAF1), onSecondaryContainer = RoseInk,
    tertiary = Rose, onTertiary = Color.White, tertiaryContainer = Petal, onTertiaryContainer = RoseInk,
    background = Blush, onBackground = RoseInk, surface = Color.White, onSurface = RoseInk,
    surfaceVariant = Color(0xFFFFEAF1), onSurfaceVariant = RoseMuted,
    outline = Color(0xFFAE7E92), outlineVariant = Color(0xFFF1CDDC),
    error = Color(0xFFA01C50), onError = Color.White,
    errorContainer = Petal, onErrorContainer = RoseInk, surfaceTint = Rose
)
@Composable
fun AjudanteDeCosturaTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = AtelierColors, typography = Typography,
        shapes = Shapes(extraSmall = RoundedCornerShape(12.dp), small = RoundedCornerShape(16.dp),
            medium = RoundedCornerShape(24.dp), large = RoundedCornerShape(28.dp), extraLarge = RoundedCornerShape(36.dp)),
        content = content)
}
