package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElasticityCalculatorScreen(navController: NavHostController) {
    var offsetX by remember { mutableStateOf(0f) }
    val initialWidth = 200f // em px (base da régua)
    
    val elasticity = if (offsetX > 0) {
        (offsetX / initialWidth * 100).toInt()
    } else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Elasticidade do Tecido", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFFFF2F6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                "Coloque o tecido sobre a régua rosa abaixo. Segure na ponta e estique até o limite.",
                color = Color.DarkGray,
                fontSize = 16.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Painel de Resultado
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("ELASTICIDADE ATUAL", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Text(
                        "$elasticity%",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFF4F9A)
                    )
                    
                    val statusText = when {
                        elasticity < 5 -> "Baixa (Tecido Firme)"
                        elasticity < 15 -> "Média (Algodão/Malha)"
                        elasticity < 30 -> "Alta (Lycra/Suplex)"
                        else -> "Muito Alta (Super Elástico)"
                    }
                    
                    Surface(
                        color = Color(0xFFFFE1E1),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            statusText,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = Color(0xFFFF4F9A),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Régua Interativa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val centerY = size.height / 2
                    val startX = 50f
                    
                    // Desenha a guia
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(startX, centerY),
                        end = Offset(size.width - 50f, centerY),
                        strokeWidth = 4f,
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    )
                }

                // Parte "elástica" da régua
                Box(
                    modifier = Modifier
                        .offset(x = 10.dp) // Ajuste visual
                        .width(maxOf(20.dp, (initialWidth + offsetX).dp / 2)) // Escala simplificada
                        .height(60.dp)
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFFFF4F9A), Color(0xFFFF85B6))),
                            RoundedCornerShape(8.dp)
                        )
                )

                // Cursor de arrasto
                Box(
                    modifier = Modifier
                        .offset(x = (initialWidth/2 + offsetX/2).dp + 10.dp)
                        .size(48.dp)
                        .shadow(4.dp, CircleShape)
                        .background(Color.White, CircleShape)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    offsetX = (offsetX + dragAmount.x).coerceIn(0f, 600f)
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(24.dp).background(Color(0xFFFF4F9A), CircleShape))
                }
            }

            Spacer(Modifier.weight(1f))
            
            Text(
                "Dica: Para tecidos com mais de 20% de elasticidade, use o ponto de overlock mais solto.",
                color = Color.Gray,
                fontSize = 12.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
