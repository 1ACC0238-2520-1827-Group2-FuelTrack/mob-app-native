package com.r0ggdev.fueltrack.provider.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.r0ggdev.fueltrack.provider.ui.screens.dashboard.ProviderDashboardScreen
import com.r0ggdev.fueltrack.provider.ui.screens.operators.OperatorCreateScreen
import com.r0ggdev.fueltrack.provider.ui.screens.operators.OperatorDetailScreen
import com.r0ggdev.fueltrack.provider.ui.screens.operators.OperatorEditScreen
import com.r0ggdev.fueltrack.provider.ui.screens.operators.OperatorListScreen
import com.r0ggdev.fueltrack.provider.ui.screens.orders.OrderDetailScreen
import com.r0ggdev.fueltrack.provider.ui.screens.orders.OrderListScreen
import com.r0ggdev.fueltrack.provider.ui.screens.orders.OrderStatusUpdateScreen
import com.r0ggdev.fueltrack.provider.ui.screens.profile.ProfileScreen
import com.r0ggdev.fueltrack.provider.ui.screens.vehicles.VehicleDetailScreen
import com.r0ggdev.fueltrack.provider.ui.screens.vehicles.VehicleListScreen
import com.r0ggdev.fueltrack.provider.ui.screens.vehicles.VehicleLocationUpdateScreen
import com.r0ggdev.fueltrack.provider.ui.screens.notifications.NotificationDetailScreen
import com.r0ggdev.fueltrack.provider.ui.screens.notifications.NotificationListScreen

sealed class ProviderScreen(val route: String) {
    object Dashboard : ProviderScreen("provider_dashboard")
    object Orders : ProviderScreen("provider_orders")
    object Vehicles : ProviderScreen("provider_vehicles")
    object Operators : ProviderScreen("provider_operators")
    object Notifications : ProviderScreen("provider_notifications")
    object Profile : ProviderScreen("provider_profile")
    
    object OrderDetail : ProviderScreen("provider_order_detail/{orderId}") {
        fun createRoute(orderId: String) = "provider_order_detail/$orderId"
    }
    object OrderStatusUpdate : ProviderScreen("provider_order_status/{orderId}") {
        fun createRoute(orderId: String) = "provider_order_status/$orderId"
    }
    
    object VehicleDetail : ProviderScreen("provider_vehicle_detail/{vehicleId}") {
        fun createRoute(vehicleId: String) = "provider_vehicle_detail/$vehicleId"
    }
    object VehicleLocationUpdate : ProviderScreen("provider_vehicle_location/{vehicleId}") {
        fun createRoute(vehicleId: String) = "provider_vehicle_location/$vehicleId"
    }
    
    object OperatorDetail : ProviderScreen("provider_operator_detail/{operatorId}") {
        fun createRoute(operatorId: String) = "provider_operator_detail/$operatorId"
    }
    object OperatorCreate : ProviderScreen("provider_operator_create")
    object OperatorEdit : ProviderScreen("provider_operator_edit/{operatorId}") {
        fun createRoute(operatorId: String) = "provider_operator_edit/$operatorId"
    }
    
    object NotificationDetail : ProviderScreen("provider_notification_detail/{notificationId}") {
        fun createRoute(notificationId: String) = "provider_notification_detail/$notificationId"
    }
}

@Composable
fun ProviderNavGraph(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ProviderScreen.Dashboard.route) {
            ProviderDashboardScreen(navController = navController)
        }
        composable(ProviderScreen.Orders.route) {
            OrderListScreen(navController = navController)
        }
        composable(ProviderScreen.Vehicles.route) {
            VehicleListScreen(navController = navController)
        }
        composable(ProviderScreen.Operators.route) {
            OperatorListScreen(navController = navController)
        }
        composable(ProviderScreen.Notifications.route) {
            NotificationListScreen(navController = navController)
        }
        composable(ProviderScreen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        
        composable(ProviderScreen.OrderDetail.route) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailScreen(navController = navController, orderId = orderId)
        }
        composable(ProviderScreen.OrderStatusUpdate.route) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderStatusUpdateScreen(navController = navController, orderId = orderId)
        }
        
        composable(ProviderScreen.VehicleDetail.route) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            VehicleDetailScreen(navController = navController, vehicleId = vehicleId)
        }
        composable(ProviderScreen.VehicleLocationUpdate.route) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            VehicleLocationUpdateScreen(navController = navController, vehicleId = vehicleId)
        }
        
        composable(ProviderScreen.OperatorDetail.route) { backStackEntry ->
            val operatorId = backStackEntry.arguments?.getString("operatorId") ?: ""
            OperatorDetailScreen(navController = navController, operatorId = operatorId)
        }
        composable(ProviderScreen.OperatorCreate.route) {
            OperatorCreateScreen(navController = navController)
        }
        composable(ProviderScreen.OperatorEdit.route) { backStackEntry ->
            val operatorId = backStackEntry.arguments?.getString("operatorId") ?: ""
            OperatorEditScreen(navController = navController, operatorId = operatorId)
        }
        
        composable(ProviderScreen.NotificationDetail.route) { backStackEntry ->
            val notificationId = backStackEntry.arguments?.getString("notificationId") ?: ""
            NotificationDetailScreen(navController = navController, notificationId = notificationId)
        }
    }
}

