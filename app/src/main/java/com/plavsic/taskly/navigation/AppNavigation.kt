package com.plavsic.taskly.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plavsic.taskly.ui.onboardingScreen.OnboardingScreen
import com.plavsic.taskly.ui.startScreen.StartScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding"){
            OnboardingScreen(navController = navController)
        }

        composable("start_screen"){
            StartScreen(navController = navController)
        }

    }

}