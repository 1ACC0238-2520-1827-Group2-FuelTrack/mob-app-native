package com.r0ggdev.fueltrack.provider.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.r0ggdev.fueltrack.provider.navigation.ProviderNavGraph
import com.r0ggdev.fueltrack.provider.navigation.ProviderScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderMainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val bottomNavItems = listOf(
        BottomNavItem(
            route = ProviderScreen.Dashboard.route,
            label = "Dashboard",
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            route = ProviderScreen.Orders.route,
            label = "Órdenes",
            icon = Icons.Default.List
        ),
        BottomNavItem(
            route = ProviderScreen.Vehicles.route,
            label = "Flota",
            icon = Icons.Default.Email
        ),
        BottomNavItem(
            route = ProviderScreen.Operators.route,
            label = "Operadores",
            icon = Icons.Default.Person
        ),
        BottomNavItem(
            route = ProviderScreen.Notifications.route,
            label = "Notificaciones",
            icon = Icons.Default.Notifications
        )
    )
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(ProviderScreen.Dashboard.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        ProviderNavGraph(
            navController = navController,
            startDestination = ProviderScreen.Dashboard.route
        )
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

