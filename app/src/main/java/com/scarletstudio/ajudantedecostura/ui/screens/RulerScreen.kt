package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun RulerScreen(navController: NavHostController) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("ruler", 0) }
    var factor by rememberSaveable { mutableFloatStateOf(prefs.getFloat("factor", 1f).coerceIn(.5f, 2f)) }
    var calibrated by rememberSaveable { mutableStateOf(prefs.getBoolean("calibrated", false)) }
    var calibrating by rememberSaveable { mutableStateOf(!calibrated) }
    val density = LocalDensity.current
    val pixelsPerCm = context.resources.displayMetrics.xdpi / 2.54f * factor
    val cmDp = with(density) { pixelsPerCm.toDp() }
    val rulerScroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    val paint = remember { android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.rgb(84, 32, 57) } }
    Scaffold(topBar = { AtelierTopBar("Régua de tecido", { navController.popBackStack() }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Text("Centímetros", style = MaterialTheme.typography.headlineMedium)
            Text("Deslize a régua para consultar até 200 cm. A rolagem não mede automaticamente o tecido.", color = RoseMuted)
            if (calibrating) {
                Text("Confira a escala com uma régua física. Ajuste até a linha abaixo medir exatamente 5 cm.")
                Box(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                    Canvas(Modifier.width(cmDp * 5 + 8.dp).height(44.dp)) {
                        val start = 4.dp.toPx(); val end = start + pixelsPerCm * 5
                        drawLine(Rose, Offset(start, 20.dp.toPx()), Offset(end, 20.dp.toPx()), 2.dp.toPx())
                        listOf(start, end).forEach { drawLine(Rose, Offset(it, 8.dp.toPx()), Offset(it, 32.dp.toPx()), 2.dp.toPx()) }
                    }
                }
                Text("5 cm", style = MaterialTheme.typography.labelLarge)
                Slider(value = factor, onValueChange = { factor = it }, valueRange = .5f..2f)
                Button(onClick = { prefs.edit().putFloat("factor", factor).putBoolean("calibrated", true).apply(); calibrated = true; calibrating = false }) { Text("Confirmar escala") }
            }
            Text(if (calibrated) "Escala conferida por você" else "Escala ainda não calibrada", color = Rose)
            Surface(color = Petal, shape = MaterialTheme.shapes.small) {
                Box(Modifier.fillMaxWidth().horizontalScroll(rulerScroll)) {
                    Canvas(Modifier.width(cmDp * 200 + 24.dp).height(110.dp)) {
                        paint.textSize = 13.dp.toPx()
                        val first = (rulerScroll.value / pixelsPerCm * 10).toInt().coerceAtLeast(0)
                        val visible = ((context.resources.displayMetrics.widthPixels / pixelsPerCm) * 10).toInt() + 30
                        for (mm in first..minOf(2000, first + visible)) {
                            val x = 12.dp.toPx() + mm * pixelsPerCm / 10
                            val major = mm % 10 == 0
                            drawLine(Rose, Offset(x, 0f), Offset(x, (if (major) 44 else if (mm % 5 == 0) 32 else 20).dp.toPx()), 1.dp.toPx())
                            if (major) drawContext.canvas.nativeCanvas.drawText((mm / 10).toString(), x, 70.dp.toPx(), paint)
                        }
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { scope.launch { rulerScroll.animateScrollTo(0) } }) { Text("Voltar ao zero") }
                TextButton(onClick = { calibrating = true }) { Text("Calibrar") }
            }
            Text("Para medir diretamente na tela, use apenas o trecho visível depois de calibrar. Para comprimentos maiores, use fita métrica.", style = MaterialTheme.typography.bodySmall, color = RoseMuted)
        }
    }
}
