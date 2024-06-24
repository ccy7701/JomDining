package com.example.jomdining.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Index
import com.example.jomdining.ui.viewmodels.FoodOrderingViewModel
import com.example.jomdining.ui.viewmodels.JomDiningSharedViewModel
import com.example.jomdining.ui.viewmodels.MenuManagementViewModel
import com.example.jomdining.ui.viewmodels.OrderHistoryViewModel
import com.example.jomdining.ui.viewmodels.RegisterViewModel

@Composable
fun NavigationGraph(startDestination: String = "login") {
    val navController = rememberNavController()
    val sharedViewModel: JomDiningSharedViewModel = viewModel(factory = JomDiningSharedViewModel.factory)
    val menuManagementViewModel: MenuManagementViewModel = viewModel(factory = MenuManagementViewModel.factory)
    val jomDiningSharedViewModel: JomDiningSharedViewModel = viewModel(factory = JomDiningSharedViewModel.factory)
    val registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.factory)
    val foodOrderingViewModel: FoodOrderingViewModel = viewModel(factory = FoodOrderingViewModel.factory)
    val orderHistoryViewModel: OrderHistoryViewModel = viewModel(factory = OrderHistoryViewModel.factory)

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                viewModel = jomDiningSharedViewModel,
                navController = navController
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = registerViewModel,
                navController = navController
            )
        }
        composable("main_menu") {
            MainMenuScreen(
                viewModel = sharedViewModel,
                navController = navController
            )
        }
        composable("food_ordering") {
            FoodOrderingModuleScreen(
                sharedViewModel = sharedViewModel,
                viewModel = foodOrderingViewModel,
                navController = navController
            )
        }
        composable("order_history") {
            OrderHistoryModuleScreen(
                sharedViewModel = sharedViewModel,
                viewModel = orderHistoryViewModel,
                navController = navController
            )
        }
        composable("order_tracking") {
            OrderTrackingModuleScreen(
                viewModel = sharedViewModel,
                navController = navController
            )
        }
        composable("menu_management") {
            MenuManagementModuleScreen(
                viewModel = menuManagementViewModel,
                navController = navController
            )
        }
    }
}
