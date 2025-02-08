package com.plavsic.taskly.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.ui.categoryScreen.CategoryScreen
import com.plavsic.taskly.ui.loginScreen.LoginScreen
import com.plavsic.taskly.ui.onboardingScreen.OnboardingScreen
import com.plavsic.taskly.ui.registerScreen.RegisterScreen
import com.plavsic.taskly.ui.shared.task.DialogViewModel
import com.plavsic.taskly.ui.startScreen.StartScreen
import com.plavsic.taskly.ui.taskScreen.TaskScreen
import com.plavsic.taskly.utils.PreferenceUtils
import com.plavsic.taskly.utils.gson.GsonInstance


@Composable
fun AppNavigation(
    navigationViewModel: NavigationViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val context = LocalContext.current

    val startDestination = if(navigationViewModel.isLoggedIn.value && PreferenceUtils.hasUserCompletedOnboarding(context)) {
        NavigationGraph.MainScreen.route
    }else if(!PreferenceUtils.hasUserCompletedOnboarding(context)){
        NavigationGraph.OnboardingScreen.route
    }else {
        NavigationGraph.StartScreen.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
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
        composable(NavigationGraph.MainScreen.route) {
            BottomNavigationBar(navController = navController, dialogViewModel = dialogViewModel)
        }
        composable(NavigationGraph.CategoryScreen.route){
            CategoryScreen(navController = navController,dialogViewModel = dialogViewModel)
        }
        composable(
            route = "${NavigationGraph.TaskScreen.route}/{task}",
            arguments = listOf(navArgument("task") { type = NavType.StringType })
        ){
            val taskJson = it.arguments?.getString("task")
            val task = GsonInstance.gson.fromJson(taskJson,Task::class.java)
            TaskScreen(task = task, navController = navController)
        }
    }
}


sealed class NavigationGraph(val route:String) {
    data object OnboardingScreen : NavigationGraph(route = "onboarding_screen")
    data object StartScreen : NavigationGraph(route = "start_screen")
    data object LoginScreen : NavigationGraph(route = "login_screen")
    data object RegisterScreen : NavigationGraph(route = "register_screen")
    data object MainScreen : NavigationGraph(route = "main_screen")
    data object CategoryScreen : NavigationGraph(route = "category_screen")
    data object TaskScreen : NavigationGraph(route = "task_screen")
}

