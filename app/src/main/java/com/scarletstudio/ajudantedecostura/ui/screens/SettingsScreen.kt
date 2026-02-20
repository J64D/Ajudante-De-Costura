package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.scarletstudio.ajudantedecostura.R
import com.scarletstudio.ajudantedecostura.navigation.Routes

// -----------------------------------------------------------------------------
// ▶ BANNER ADMOB
// -----------------------------------------------------------------------------
@Composable
fun AdMobBanner() {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), // 🔒 NÃO ALTERADO
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-1367515717522293/6625687240"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

// -----------------------------------------------------------------------------
// ▶ SETTINGS SCREEN
// -----------------------------------------------------------------------------
@Composable
fun SettingsScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ---------------------------------------------------------------------
        // ▶ CABEÇALHO
        // ---------------------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Configurações",
                fontSize = 18.sp, // AJUSTE
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF4F9A)
            )

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Voltar",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp) // AJUSTE
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp)) // AJUSTE

        AdMobBanner()

        Spacer(modifier = Modifier.height(12.dp)) // AJUSTE

        // ---------------------------------------------------------------------
        // ▶ BOTÃO DE PRIVACIDADE
        // ---------------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp), // AJUSTE
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { navController.navigate(Routes.PRIVACY) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFE0EB),
                    contentColor = Color(0xFFFF4F9A)
                ),
                shape = CircleShape,
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier.height(34.dp) // AJUSTE
            ) {
                Text("Privacidade", fontSize = 13.sp) // AJUSTE
            }
        }

        Spacer(modifier = Modifier.height(10.dp)) // AJUSTE

        // ---------------------------------------------------------------------
        // ▶ SEÇÃO DE GESTÃO DE ESTOQUE
        // ---------------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF3F7), RoundedCornerShape(14.dp)) // AJUSTE
                .padding(14.dp) // AJUSTE
        ) {

            Text(
                text = "GESTÃO DE ESTOQUE",
                fontSize = 13.sp, // AJUSTE
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF4F9A)
            )

            Spacer(modifier = Modifier.height(10.dp)) // AJUSTE

            // -----------------------------------------------------------------
            // ▶ EXPORTAR & IMPORTAR
            // -----------------------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* exportar */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE0EB)),
                    shape = RoundedCornerShape(14.dp), // AJUSTE
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp) // AJUSTE
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_box),
                        contentDescription = "Exportar",
                        tint = Color(0xFFFF4F9A),
                        modifier = Modifier.size(18.dp) // AJUSTE
                    )
                    Spacer(modifier = Modifier.width(4.dp)) // AJUSTE
                    Text("Exportar", color = Color(0xFFFF4F9A), fontSize = 13.sp) // AJUSTE
                }

                Spacer(modifier = Modifier.width(10.dp)) // AJUSTE

                Button(
                    onClick = { /* importar */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE0EB)),
                    shape = RoundedCornerShape(14.dp), // AJUSTE
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp) // AJUSTE
                ) {
                    Text("Importar", color = Color(0xFFFF4F9A), fontSize = 13.sp) // AJUSTE
                }
            }

            Spacer(modifier = Modifier.height(14.dp)) // AJUSTE

            // -----------------------------------------------------------------
            // ▶ BOTÃO "LIMPAR TODO O ESTOQUE"
            // -----------------------------------------------------------------
            Button(
                onClick = { /* limpar */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC8DA)),
                shape = RoundedCornerShape(18.dp), // AJUSTE
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp) // AJUSTE (antes 85.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(4.dp)) // AJUSTE

                    Icon(
                        painter = painterResource(id = R.drawable.ic_trash),
                        contentDescription = "Lixeira",
                        tint = Color(0xFFFF4F9A),
                        modifier = Modifier.size(30.dp) // AJUSTE
                    )

                    Spacer(modifier = Modifier.width(12.dp)) // AJUSTE

                    Text(
                        "Limpar Todo o Estoque",
                        color = Color(0xFFFF4F9A),
                        fontSize = 14.sp, // AJUSTE
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
