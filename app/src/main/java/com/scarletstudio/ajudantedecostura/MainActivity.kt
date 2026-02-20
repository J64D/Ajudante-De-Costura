package com.scarletstudio.ajudantedecostura

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.scarletstudio.ajudantedecostura.navigation.Navigation

class MainActivity : ComponentActivity() {

    companion object {
        init {
            try {
                // ✅ OpenCV via NDK — carregamento MANUAL obrigatório
                System.loadLibrary("opencv_java4")
                Log.d("OpenCV", "✅ OpenCV carregado com sucesso")
            } catch (e: UnsatisfiedLinkError) {
                Log.e("OpenCV", "❌ Erro ao carregar OpenCV", e)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // ✅ Android 15 / SDK 35 – Edge-to-Edge seguro
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        // ✅ Inicialização do AdMob
        MobileAds.initialize(this) { status ->
            Log.d("AdMob", "✅ AdMob inicializado: $status")
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    Navigation(navController)
                }
            }
        }
    }
}
