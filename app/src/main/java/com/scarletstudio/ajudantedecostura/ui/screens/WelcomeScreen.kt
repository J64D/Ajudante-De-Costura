package com.scarletstudio.ajudantedecostura.ui.screens

import android.app.Activity
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.scarletstudio.ajudantedecostura.R
import com.scarletstudio.ajudantedecostura.navigation.Routes

@Composable
fun WelcomeScreen(navController: NavHostController) {

    val context = LocalContext.current
    val activity = context as Activity

    // ✅ STATE CORRETO
    var interstitialAd: InterstitialAd? by remember { mutableStateOf(null) }

    // ================= LOAD INTERSTITIAL =================
    LaunchedEffect(Unit) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            activity,
            "ca-app-pub-1367515717522293/3945669116",
            adRequest,
            object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    Log.d("AdMob", "✅ Interstitial carregado")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    Log.e("AdMob", "❌ Erro Interstitial: ${error.message}")
                }
            }
        )
    }

    fun showAdThenNavigate() {
        val ad = interstitialAd

        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    navController.navigate(Routes.HOME)
                }
            }

            ad.show(activity)
        } else {
            navController.navigate(Routes.HOME)
        }
    }

    // ================= UI =================

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        label = "button-scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFF2F6), Color.White)
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .padding(32.dp)
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_costura_logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(95.dp)
            )

            Spacer(Modifier.height(20.dp))

            Text("Ajudante de", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                "Costura",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF4F9A)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Seu ateliê organizado.\nGerencie peças, cores e\nquantidades com simplicidade.",
                color = Color.DarkGray,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(22.dp))

            Button(
                onClick = { showAdThenNavigate() },
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF4F9A)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .scale(buttonScale)
            ) {
                Text("Iniciar", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
