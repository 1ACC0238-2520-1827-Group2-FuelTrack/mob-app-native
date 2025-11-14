package com.r0ggdev.fueltrack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.r0ggdev.fueltrack.ui.screens.auth.LoginScreen
import com.r0ggdev.fueltrack.ui.screens.auth.SignupScreen
import com.r0ggdev.fueltrack.ui.screens.auth.SplashScreen
import com.r0ggdev.fueltrack.ui.screens.dashboard.DashboardScreen
import com.r0ggdev.fueltrack.ui.screens.expense.ExpenseDetailScreen
import com.r0ggdev.fueltrack.ui.screens.expense.ExpenseEditScreen
import com.r0ggdev.fueltrack.ui.screens.expense.ExpenseListScreen
import com.r0ggdev.fueltrack.ui.screens.expense.ExpenseCreateScreen
import com.r0ggdev.fueltrack.ui.screens.fuel.FuelRecordCreateScreen
import com.r0ggdev.fueltrack.ui.screens.fuel.FuelRecordDetailScreen
import com.r0ggdev.fueltrack.ui.screens.fuel.FuelRecordEditScreen
import com.r0ggdev.fueltrack.ui.screens.fuel.FuelRecordListScreen
import com.r0ggdev.fueltrack.ui.screens.vehicle.VehicleCreateScreen
import com.r0ggdev.fueltrack.ui.screens.vehicle.VehicleDetailScreen
import com.r0ggdev.fueltrack.ui.screens.vehicle.VehicleEditScreen
import com.r0ggdev.fueltrack.ui.screens.vehicle.VehicleListScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Dashboard : Screen("dashboard")
    
    object VehicleList : Screen("vehicle_list")
    object VehicleCreate : Screen("vehicle_create")
    object VehicleEdit : Screen("vehicle_edit/{vehicleId}") {
        fun createRoute(vehicleId: String) = "vehicle_edit/$vehicleId"
    }
    object VehicleDetail : Screen("vehicle_detail/{vehicleId}") {
        fun createRoute(vehicleId: String) = "vehicle_detail/$vehicleId"
    }
    
    object FuelRecordList : Screen("fuel_record_list/{vehicleId}") {
        fun createRoute(vehicleId: String) = "fuel_record_list/$vehicleId"
    }
    object FuelRecordCreate : Screen("fuel_record_create/{vehicleId}") {
        fun createRoute(vehicleId: String) = "fuel_record_create/$vehicleId"
    }
    object FuelRecordEdit : Screen("fuel_record_edit/{recordId}") {
        fun createRoute(recordId: String) = "fuel_record_edit/$recordId"
    }
    object FuelRecordDetail : Screen("fuel_record_detail/{recordId}") {
        fun createRoute(recordId: String) = "fuel_record_detail/$recordId"
    }
    
    object ExpenseList : Screen("expense_list/{vehicleId}") {
        fun createRoute(vehicleId: String) = "expense_list/$vehicleId"
    }
    object ExpenseCreate : Screen("expense_create/{vehicleId}") {
        fun createRoute(vehicleId: String) = "expense_create/$vehicleId"
    }
    object ExpenseEdit : Screen("expense_edit/{expenseId}") {
        fun createRoute(expenseId: String) = "expense_edit/$expenseId"
    }
    object ExpenseDetail : Screen("expense_detail/{expenseId}") {
        fun createRoute(expenseId: String) = "expense_detail/$expenseId"
    }
}

@Composable
fun NavGraph(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Signup.route) {
            SignupScreen(navController = navController)
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        
        composable(Screen.VehicleList.route) {
            VehicleListScreen(navController = navController)
        }
        composable(Screen.VehicleCreate.route) {
            VehicleCreateScreen(navController = navController)
        }
        composable(Screen.VehicleEdit.route) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            VehicleEditScreen(navController = navController, vehicleId = vehicleId)
        }
        composable(Screen.VehicleDetail.route) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            VehicleDetailScreen(navController = navController, vehicleId = vehicleId)
        }
        
        composable(Screen.FuelRecordList.route) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            FuelRecordListScreen(navController = navController, vehicleId = vehicleId)
        }
        composable(Screen.FuelRecordCreate.route) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            FuelRecordCreateScreen(navController = navController, vehicleId = vehicleId)
        }
        composable(Screen.FuelRecordEdit.route) { backStackEntry ->
            val recordId = backStackEntry.arguments?.getString("recordId") ?: ""
            FuelRecordEditScreen(navController = navController, recordId = recordId)
        }
        composable(Screen.FuelRecordDetail.route) { backStackEntry ->
            val recordId = backStackEntry.arguments?.getString("recordId") ?: ""
            FuelRecordDetailScreen(navController = navController, recordId = recordId)
        }
        
        composable(Screen.ExpenseList.route) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            ExpenseListScreen(navController = navController, vehicleId = vehicleId)
        }
        composable(Screen.ExpenseCreate.route) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            ExpenseCreateScreen(navController = navController, vehicleId = vehicleId)
        }
        composable(Screen.ExpenseEdit.route) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString("expenseId") ?: ""
            ExpenseEditScreen(navController = navController, expenseId = expenseId)
        }
        composable(Screen.ExpenseDetail.route) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString("expenseId") ?: ""
            ExpenseDetailScreen(navController = navController, expenseId = expenseId)
        }
    }
}

