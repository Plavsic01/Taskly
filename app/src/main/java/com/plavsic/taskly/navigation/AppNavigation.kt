package com.plavsic.taskly.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plavsic.taskly.ui.categoryScreen.CategoryScreen
import com.plavsic.taskly.ui.loginScreen.LoginScreen
import com.plavsic.taskly.ui.onboardingScreen.OnboardingScreen
import com.plavsic.taskly.ui.registerScreen.RegisterScreen
import com.plavsic.taskly.ui.shared.task.DialogViewModel
import com.plavsic.taskly.ui.startScreen.StartScreen
import com.plavsic.taskly.ui.taskScreen.TaskScreen


@Composable
fun AppNavigation(
    navigationViewModel: NavigationViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel()
) {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
//        enterTransition = {
//            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700))
//        },
//        exitTransition = {
//            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700))
//        },
        startDestination = if(navigationViewModel.isLoggedIn.value)
            NavigationGraph.BottomNavigationBar.route
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
        composable(NavigationGraph.BottomNavigationBar.route) {
            BottomNavigationBar(navController = navController,dialogViewModel = dialogViewModel)
        }
        composable(NavigationGraph.CategoryScreen.route){
            CategoryScreen(navController = navController,dialogViewModel = dialogViewModel)
        }
        composable(NavigationGraph.TaskScreen.route){
            TaskScreen()
        }
    }
}


sealed class NavigationGraph(val route:String) {
    data object OnboardingScreen : NavigationGraph(route = "onboarding_screen")
    data object StartScreen : NavigationGraph(route = "start_screen")
    data object LoginScreen : NavigationGraph(route = "login_screen")
    data object RegisterScreen : NavigationGraph(route = "register_screen")
    data object BottomNavigationBar : NavigationGraph(route = "bottom_navigation_bar")
    data object CategoryScreen : NavigationGraph(route = "category_screen")
    data object TaskScreen : NavigationGraph(route = "task_screen")
}

