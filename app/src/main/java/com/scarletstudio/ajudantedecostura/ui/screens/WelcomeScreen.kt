package com.scarletstudio.ajudantedecostura.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import android.content.ContextWrapper
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.scarletstudio.ajudantedecostura.BuildConfig
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.navigation.Routes
import com.scarletstudio.ajudantedecostura.ui.theme.*

@Composable
fun WelcomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val activity = remember(context) {
        generateSequence(context) { (it as? ContextWrapper)?.baseContext }
            .filterIsInstance<Activity>().firstOrNull()
    }
    var interstitial by remember { mutableStateOf<InterstitialAd?>(null) }
    var entering by remember { mutableStateOf(false) }
    var completed by remember { mutableStateOf(false) }
    var active by remember { mutableStateOf(true) }
    DisposableEffect(context) {
        active = true
        InterstitialAd.load(context,
            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712" else "ca-app-pub-1367515717522293/3945669116",
            AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    if (active && !entering) interstitial = ad
                }
                override fun onAdFailedToLoad(error: LoadAdError) { interstitial = null }
            })
        onDispose {
            active = false
            interstitial?.fullScreenContentCallback = null
            interstitial = null
        }
    }
    fun openAtelier() {
        if (!active || completed) return
        completed = true
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.WELCOME) { inclusive = true }
            launchSingleTop = true
        }
    }
    fun enter() {
        if (entering) return
        entering = true
        val ad = interstitial
        interstitial = null
        if (ad == null || activity == null || activity.isFinishing || activity.isDestroyed) {
            openAtelier()
            return
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() { ad.fullScreenContentCallback = null; openAtelier() }
            override fun onAdFailedToShowFullScreenContent(error: AdError) { ad.fullScreenContentCallback = null; openAtelier() }
        }
        try { ad.show(activity) } catch (_: Exception) {
            ad.fullScreenContentCallback = null
            openAtelier()
        }
    }
    Scaffold(containerColor = Blush, bottomBar = {
        Surface(color = Blush) {
            Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { enter() }, enabled = !entering,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)) { Text("Entrar no ateliê") }
                TextButton(enabled = !entering, onClick = { navController.navigate(Routes.PRIVACY) }) { Text("Privacidade") }
            }
        }
    }) { padding ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(padding)) {
            val compact = maxHeight < 420.dp
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                BrandLogo(Modifier.size(if (compact) 88.dp else 144.dp))
                Spacer(Modifier.height(24.dp))
                Text("Ajudante de Costura", style = MaterialTheme.typography.headlineMedium, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                Text("Peças, medidas e moldes.\nTudo no mesmo lugar.", color = RoseMuted,
                    style = MaterialTheme.typography.bodyLarge, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }
    }
}


