package com.example.jomdining.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jomdining.ui.viewmodels.JomDiningViewModel
import com.example.jomdining.ui.viewmodels.MenuManagementViewModel

@Composable
fun NavigationGraph(startDestination: String = "login") {
    val navController = rememberNavController()
    val viewModel: JomDiningViewModel = viewModel(factory = JomDiningViewModel.factory)
    val menuManagementViewModel: MenuManagementViewModel = viewModel(factory = MenuManagementViewModel.factory)

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginScreen(viewModel = viewModel, navController = navController) }
        composable("register") { RegisterScreen(viewModel = viewModel, navController = navController) }
        composable("main_menu") { MainMenuScreen(viewModel = viewModel, navController = navController) }
        composable("food_ordering") { FoodOrderingModuleScreen(viewModel = viewModel, navController = navController) }
        composable("order_history") { OrderHistoryModuleScreen(viewModel = viewModel, navController = navController) }
        composable("order_tracking") { OrderTrackingModuleScreen(viewModel = viewModel, navController = navController) }
        composable("menu_management") {
            MenuManagementModuleScreen(
                viewModel = menuManagementViewModel,
                navController = navController
            )
        }
    }
}
