package com.scarletstudio.ajudantedecostura.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.scarletstudio.ajudantedecostura.ui.screens.*

object Routes {
    const val WELCOME = "welcome"
    const val HOME = "home"
    const val ADD_PIECE = "add_piece"
    const val SETTINGS = "settings"
    const val PRIVACY = "privacy"
    const val ORDER_SUMMARY = "order_summary"
    const val PRODUCTION = "production"
    const val FABRICS = "fabrics"
    const val LEARN = "learn"

    // 🛠️ FERRAMENTAS PRO
    const val ELASTICITY = "elasticity"
    const val PRICING = "pricing"
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME,
        enterTransition = { fadeIn(animationSpec = tween(400)) + slideInHorizontally(initialOffsetX = { it / 10 }) },
        exitTransition = { fadeOut(animationSpec = tween(400)) + slideOutHorizontally(targetOffsetX = { -it / 10 }) },
        popEnterTransition = { fadeIn(animationSpec = tween(400)) + slideInHorizontally(initialOffsetX = { -it / 10 }) },
        popExitTransition = { fadeOut(animationSpec = tween(400)) + slideOutHorizontally(targetOffsetX = { it / 10 }) }
    ) {

        composable(Routes.WELCOME) {
            WelcomeScreen(navController)
        }

        composable(Routes.HOME) {
            AtelierDashboardScreen(navController)
        }

        composable(Routes.PRODUCTION) {
            HomeScreen(navController)
        }

        composable(Routes.ADD_PIECE) {
            AddPieceScreen(navController)
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(navController)
        }

        composable(Routes.PRIVACY) {
            PrivacyPolicyScreen(navController)
        }

        // 📦 RESUMO DO DIA
        composable(Routes.ORDER_SUMMARY) {
            OrderSummaryScreen(navController)
        }

        // 🛠️ FERRAMENTAS PRO
        composable(Routes.ELASTICITY) {
            ElasticityTestScreen(navController)
        }

        composable(Routes.PRICING) {
            PriceCalculatorScreen(navController)
        }

        composable(Routes.FABRICS) {
            FabricGuideScreen(navController)
        }

        composable(Routes.LEARN) {
            LearnScreen(navController)
        }
    }
}
