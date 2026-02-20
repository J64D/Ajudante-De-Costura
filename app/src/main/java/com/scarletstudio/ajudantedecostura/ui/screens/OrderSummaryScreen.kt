package com.scarletstudio.ajudantedecostura.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.R
import com.scarletstudio.ajudantedecostura.data.local.PieceDatabase
import com.scarletstudio.ajudantedecostura.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun OrderSummaryScreen(navController: NavHostController) {

    val context = LocalContext.current
    val dao = remember { PieceDatabase.getDatabase(context).pieceDao() }
    val pieces by dao.getAll().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    val totalGeral = pieces.sumOf { it.quantity }

    // ----------------------
    // TEXTO PARA WHATSAPP
    // ----------------------
    val message = buildString {
        append("✂️❤️ *Peças terminadas hoje* ❤️✂️\n\n")
        if (pieces.isEmpty()) {
            append("Nenhuma peça cadastrada.\n")
        } else {
            pieces.forEach { piece ->
                append("• *${piece.name}* - ${piece.color}: ${piece.quantity}\n")
            }
        }
        append("\n*Total Geral: $totalGeral peças*")
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Surface(
                color = Color.White,
                modifier = Modifier.statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Resumo do Dia",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF4F9A)
                    )

                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Fechar",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(20.dp)
        ) {

            // ----------------------
            // CARD DO RESUMO
            // ----------------------
            Card(
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        "✂️ Peças terminadas hoje",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )

                    if (pieces.isEmpty()) {
                        Text("Nenhuma peça cadastrada.", color = Color.Gray)
                    } else {
                        pieces.forEach { piece ->
                            Text(
                                "• ${piece.name} - ${piece.color}: ${piece.quantity}",
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                        }
                    }

                    Divider()

                    Text(
                        "Total Geral: $totalGeral peças",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFFFF4F9A)
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            // ----------------------
            // BOTÃO WHATSAPP
            // ----------------------
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW)
                    val url = "https://wa.me/?text=" + Uri.encode(message)
                    intent.data = Uri.parse(url)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
            ) {
                Text("Enviar pelo WhatsApp", color = Color.White, fontSize = 16.sp)
            }

            Spacer(Modifier.height(15.dp))

            // ----------------------
            // BOTÃO NOVO PEDIDO
            // (ZERA TUDO E VOLTA PARA HOME)
            // ----------------------
            Button(
                onClick = {
                    scope.launch {
                        dao.deleteAll()

                        // Fecha TODAS as telas e volta para a HOME sem crash
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFCDD9)
                )
            ) {
                Text("Iniciar Novo Pedido", color = Color(0xFFFF4F9A), fontSize = 16.sp)
            }
        }
    }
}
