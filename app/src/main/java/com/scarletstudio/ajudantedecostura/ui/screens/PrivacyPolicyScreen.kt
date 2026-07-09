package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

// -----------------------------------------------------------------------------
// ▶ BANNER ADMOB
// -----------------------------------------------------------------------------
@Composable
fun AdMobBannerPrivacy() {
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

@Composable
fun PrivacyPolicyScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFDFD))
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp)
            .padding(top = 12.dp)
    ) {

        // TOP BAR
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Text("←", fontSize = 22.sp, color = Color(0xFFFF4F9A)) // AJUSTE
            }
            Text(
                "Política de Privacidade",
                fontSize = 18.sp, // AJUSTE
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(start = 4.dp) // AJUSTE
            )
        }

        Spacer(modifier = Modifier.height(12.dp)) // AJUSTE

        // ▶ ADMOB
        AdMobBannerPrivacy()

        Spacer(modifier = Modifier.height(16.dp)) // AJUSTE

        // ÍCONE CENTRAL
        Box(
            modifier = Modifier
                .size(90.dp) // AJUSTE
                .clip(CircleShape)
                .background(Color(0xFFFFE8F1))
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Text("🛡️", fontSize = 36.sp) // AJUSTE
        }

        Spacer(modifier = Modifier.height(12.dp)) // AJUSTE

        // TÍTULO
        Text(
            "Transparência e Segurança",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 18.sp, // AJUSTE
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "Aqui explicamos claramente como seus dados são protegidos durante o uso do aplicativo.",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 13.sp, // AJUSTE
            color = Color.Gray,
            lineHeight = 18.sp // AJUSTE
        )

        Spacer(modifier = Modifier.height(12.dp)) // AJUSTE

        // DATA
        Surface(
            color = Color(0xFFFFEDF4),
            shape = RoundedCornerShape(50),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "ÚLTIMA ATUALIZAÇÃO: 11/12/2025",
                fontSize = 10.sp, // AJUSTE
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp), // AJUSTE
                color = Color(0xFF444444)
            )
        }

        Spacer(modifier = Modifier.height(20.dp)) // AJUSTE

        PrivacyCard(
            emoji = "📁",
            title = "1. Dados Coletados pelo Aplicativo",
            text = "O aplicativo em si NÃO coleta, armazena ou transmite dados pessoais seus. " +
                    "Todas as informações de costura (medidas, tecidos, projetos) ficam " +
                    "salvas apenas localmente no seu dispositivo e nunca são enviadas a servidores externos."
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrivacyCard(
            emoji = "📊",
            title = "2. Dados Coletados pelo Google AdMob",
            text = "Para exibir anúncios relevantes, o Google AdMob pode coletar automaticamente:\n\n" +
                    "• Identificadores de dispositivo (ID de publicidade Android / GAID)\n" +
                    "• Localização aproximada (baseada em IP)\n" +
                    "• Informações de diagnóstico e logs de falhas do app\n" +
                    "• Dados de interação com anúncios (cliques, visualizações)\n" +
                    "• Informações do dispositivo (modelo, versão do sistema operacional)\n\n" +
                    "Esses dados são tratados pelo Google conforme a Política de Privacidade do Google " +
                    "(policies.google.com/privacy)."
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrivacyCard(
            emoji = "🎯",
            title = "3. Finalidade do Uso dos Dados",
            text = "Os dados coletados pelo AdMob são utilizados exclusivamente para:\n\n" +
                    "• Exibir anúncios personalizados ou não personalizados\n" +
                    "• Medir o desempenho dos anúncios\n" +
                    "• Prevenir fraudes e melhorar a segurança\n" +
                    "• Gerar relatórios estatísticos agregados\n\n" +
                    "O aplicativo NÃO usa esses dados para qualquer outra finalidade."
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrivacyCard(
            emoji = "💾",
            title = "4. Armazenamento e Retenção",
            text = "Dados do app: armazenados apenas localmente no seu dispositivo, sem prazo de expiração, " +
                    "removidos ao desinstalar o aplicativo.\n\n" +
                    "Dados do AdMob: retidos pelo Google conforme as políticas deles " +
                    "(geralmente até 18 meses). Consulte o Google para gerenciar seus dados."
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrivacyCard(
            emoji = "🔒",
            title = "5. Segurança dos Dados",
            text = "Todas as informações geradas no aplicativo ficam protegidas pelo sistema " +
                    "de segurança do seu próprio dispositivo Android. " +
                    "Não realizamos transmissão de dados pessoais por nossa parte. " +
                    "As transmissões feitas pelo AdMob utilizam criptografia TLS/HTTPS."
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrivacyCard(
            emoji = "👤",
            title = "6. Seus Direitos (LGPD)",
            text = "Conforme a Lei Geral de Proteção de Dados (LGPD — Lei nº 13.709/2018), você tem direito a:\n\n" +
                    "• Saber quais dados são coletados sobre você\n" +
                    "• Solicitar a exclusão dos seus dados\n" +
                    "• Revogar o consentimento a qualquer momento\n" +
                    "• Optar por anúncios não personalizados (nas configurações do Google no seu dispositivo)\n\n" +
                    "Para exercer esses direitos referentes ao AdMob, acesse: " +
                    "myaccount.google.com/data-and-privacy"
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrivacyCard(
            emoji = "🌐",
            title = "7. Publicidade (Google AdMob)",
            text = "Este aplicativo é parceiro do Google AdMob (Google LLC, 1600 Amphitheatre Pkwy, " +
                    "Mountain View, CA 94043, EUA). O AdMob segue o " +
                    "Programa de Conformidade da IAB e cumpre as regulamentações GDPR e CCPA. " +
                    "Mais informações: admob.google.com"
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrivacyCard(
            emoji = "📧",
            title = "8. Contato",
            text = "Dúvidas sobre esta política de privacidade?\n\n" +
                    "E-mail: scarletstudiobrasil@gmail.com\n\n" +
                    "Respondemos em até 5 dias úteis."
        )

        Spacer(modifier = Modifier.height(28.dp)) // AJUSTE
    }
}

// -----------------------------------------------------------------------------
// ▶ CARD
// -----------------------------------------------------------------------------
@Composable
fun PrivacyCard(
    emoji: String,
    title: String,
    text: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp), // AJUSTE
        shadowElevation = 3.dp, // AJUSTE
        tonalElevation = 2.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) { // AJUSTE

            Box(
                modifier = Modifier
                    .size(38.dp) // AJUSTE
                    .clip(RoundedCornerShape(10.dp)) // AJUSTE
                    .background(Color(0xFFFFF4F7)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 22.sp) // AJUSTE
            }

            Spacer(modifier = Modifier.height(10.dp)) // AJUSTE

            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold) // AJUSTE

            Spacer(modifier = Modifier.height(4.dp)) // AJUSTE

            Text(text, fontSize = 13.sp, lineHeight = 18.sp) // AJUSTE
        }
    }
}
