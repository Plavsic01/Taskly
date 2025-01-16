package com.plavsic.taskly.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.plavsic.taskly.ui.homeScreen.HomeScreen
import com.plavsic.taskly.ui.loginScreen.LoginScreen
import com.plavsic.taskly.ui.onboardingScreen.OnboardingScreen
import com.plavsic.taskly.ui.registerScreen.RegisterScreen
import com.plavsic.taskly.ui.startScreen.StartScreen


@Composable
fun AppNavigation(
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

//    Log.i("PHOTO",FirebaseAuth.getInstance().currentUser!!.photoUrl.toString())

    NavHost(navController = navController, startDestination = if(navigationViewModel.isLoggedIn.value)
        NavigationGraph.HomeScreen.route
    else
        NavigationGraph.OnboardingScreen.route
    ) {
        composable(NavigationGraph.OnboardingScreen.route){
            OnboardingScreen(navController = navController)
        }

        composable(NavigationGraph.StartScreen.route){
            StartScreen(navController = navController)
        }

        composable(NavigationGraph.LoginScreen.route){
            LoginScreen(navController = navController)
        }
        composable(NavigationGraph.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(NavigationGraph.HomeScreen.route) {
            BottomNavigationBar()
        }

    }

}


sealed class NavigationGraph(val route:String) {
    data object OnboardingScreen : NavigationGraph(route = "onboarding_screen")
    data object StartScreen : NavigationGraph(route = "start_screen")
    data object LoginScreen : NavigationGraph(route = "login_screen")
    data object RegisterScreen : NavigationGraph(route = "register_screen")
    data object HomeScreen : NavigationGraph(route = "home_screen")
}

