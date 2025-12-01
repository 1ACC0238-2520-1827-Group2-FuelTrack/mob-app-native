package com.r0ggdev.fueltrack.provider.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.r0ggdev.fueltrack.navigation.Screen
import com.r0ggdev.fueltrack.provider.navigation.ProviderScreen
import com.r0ggdev.fueltrack.provider.ui.screens.orders.OrderDetailScreen
import com.r0ggdev.fueltrack.provider.ui.screens.orders.OrderListScreen
import com.r0ggdev.fueltrack.provider.ui.screens.vehicles.VehicleCreateScreen
import com.r0ggdev.fueltrack.provider.ui.screens.vehicles.VehicleDetailScreen
import com.r0ggdev.fueltrack.provider.ui.screens.vehicles.VehicleListScreen
import com.r0ggdev.fueltrack.provider.ui.screens.vehicles.VehicleLocationUpdateScreen
import com.r0ggdev.fueltrack.provider.ui.viewmodel.OrderViewModel
import com.r0ggdev.fueltrack.provider.ui.viewmodel.VehicleViewModel
import com.r0ggdev.fueltrack.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderMainScreen(navController: NavHostController, startDestination: String = ProviderScreen.Dashboard.route) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val authViewModel: AuthViewModel = hiltViewModel()

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
            icon = Icons.Default.Build  // Ícono básico que existe
        ),
        BottomNavItem(
            route = ProviderScreen.Operators.route,
            label = "Operadores",
            icon = Icons.Default.Person
        ),
        BottomNavItem(
            route = "logout",  // Ruta especial para logout
            label = "Salir",
            icon = Icons.AutoMirrored.Filled.ExitToApp
        )
    )

    // PASO 2: Scaffold con NavigationBar
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Provider Dashboard") })
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (item.route == "logout") {
                                // Lógica especial para logout
                                authViewModel.logout()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            } else {
                                // Navegación normal
                                navController.navigate(item.route) {
                                    popUpTo(ProviderScreen.Dashboard.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // Manejar rutas del provider directamente
            when (currentRoute) {
                ProviderScreen.Dashboard.route -> {
                    // Contenido simple para Dashboard
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🏠 Dashboard del Proveedor",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Próximo paso: agregar pantalla de Órdenes",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                ProviderScreen.Orders.route -> {
                    // LISTA REAL DE PEDIDOS CONECTADA AL BACKEND
                    val orderViewModel: OrderViewModel = hiltViewModel()
                    OrderListScreen(navController = navController, viewModel = orderViewModel)
                }
                ProviderScreen.Vehicles.route -> {
                    // LISTA DE VEHÍCULOS
                    val vehicleViewModel: VehicleViewModel = hiltViewModel()
                    VehicleListScreen(navController = navController, viewModel = vehicleViewModel)
                }
                else -> {
                    // Verificar si es una ruta con parámetros
                    when {
                        currentRoute?.startsWith("provider_order_detail/") == true -> {
                            val orderId = currentRoute?.substringAfter("provider_order_detail/") ?: ""
                            OrderDetailScreen(navController = navController, orderId = orderId)
                        }
                        currentRoute?.startsWith("provider_vehicle_detail/") == true -> {
                            val vehicleId = currentRoute?.substringAfter("provider_vehicle_detail/") ?: ""
                            VehicleDetailScreen(navController = navController, vehicleId = vehicleId)
                        }
                        currentRoute?.startsWith("provider_vehicle_location/") == true -> {
                            val vehicleId = currentRoute?.substringAfter("provider_vehicle_location/") ?: ""
                            VehicleLocationUpdateScreen(navController = navController, vehicleId = vehicleId)
                        }
                        currentRoute == "provider_vehicle_create" -> {
                            VehicleCreateScreen(navController = navController)
                        }
                        else -> {
                            // Default: Dashboard
                            Text(
                                text = "Ruta no encontrada: $currentRoute",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

