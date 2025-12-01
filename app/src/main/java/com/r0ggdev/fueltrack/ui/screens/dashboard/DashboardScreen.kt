package com.r0ggdev.fueltrack.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.navigation.Screen
import com.r0ggdev.fueltrack.ui.components.NavigationDrawer
import com.r0ggdev.fueltrack.ui.viewmodel.AuthViewModel
import com.r0ggdev.fueltrack.ui.viewmodel.DashboardViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    dashboardViewModel: DashboardViewModel
) {
    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val dashboardState = dashboardViewModel.uiState.collectAsState()
    val authState = authViewModel.uiState.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(
                userName = "${authState.value.firstName ?: ""} ${authState.value.lastName ?: ""}".trim(),
                userEmail = authState.value.email ?: "",
                userRole = authState.value.role,
                onNavigate = { screen ->
                    when (screen) {
                        "profile" -> navController.navigate("profile_screen")
                        "orders" -> {
                            // Navigate to provider orders screen
                            navController.navigate("provider_orders") {
                                popUpTo(Screen.Dashboard.route) { inclusive = true }
                            }
                        }
                    }
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "menu")
                        }
                    },
                    actions = {
                        TextButton(onClick = {
                            navController.navigate(Screen.VehicleList.route)
                        }) {
                            Text("Vehículos")
                        }

                        IconButton(onClick = {
                            authViewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                        }
                    }
                )
            },
            floatingActionButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FloatingActionButton(
                        onClick = {
                            dashboardState.value.selectedVehicleId?.let { vehicleId ->
                                navController.navigate(Screen.FuelRecordCreate.createRoute(vehicleId))
                            }
                        },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar combustible")
                    }

                    FloatingActionButton(
                        onClick = {
                            dashboardState.value.selectedVehicleId?.let { vehicleId ->
                                navController.navigate(Screen.ExpenseCreate.createRoute(vehicleId))
                            }
                        },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar gasto")
                    }
                }
            }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                
                if (dashboardState.value.vehicles.isNotEmpty()) {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = dashboardState.value.vehicles.find { it.id == dashboardState.value.selectedVehicleId }?.let {
                                "${it.brand} ${it.model} - ${it.plate}"
                            } ?: "Seleccionar vehículo",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Vehículo") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            dashboardState.value.vehicles.forEach { vehicle ->
                                DropdownMenuItem(
                                    text = { Text("${vehicle.brand} ${vehicle.model} - ${vehicle.plate}") },
                                    onClick = {
                                        dashboardViewModel.selectVehicle(vehicle.id)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }


                if (dashboardState.value.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    dashboardState.value.dashboard?.let { dashboard ->

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Card(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(Modifier.padding(16.dp)) {
                                            Text("Consumo Promedio")
                                            Text(
                                                dashboard.averageConsumption?.let {
                                                    "${String.format("%.2f", it)} L/100km"
                                                } ?: "N/A",
                                                style = MaterialTheme.typography.headlineMedium
                                            )
                                        }
                                    }

                                    Card(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(Modifier.padding(16.dp)) {
                                            Text("Costo Mensual")
                                            Text(
                                                dashboard.monthlyCost?.let {
                                                    NumberFormat.getCurrencyInstance(Locale.getDefault()).format(it)
                                                } ?: "N/A",
                                                style = MaterialTheme.typography.headlineMedium
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                dashboard.lastFuelRecord?.let { record ->
                                    Card {
                                        Column(Modifier.padding(16.dp)) {
                                            Text("Último Registro")
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("Fecha: ${record.date}")
                                            Text("Kilometraje: ${record.odometer} km")
                                            Text("Litros: ${record.liters} L")
                                            Text(
                                                "Precio: ${
                                                    NumberFormat.getCurrencyInstance(Locale.getDefault()).format(
                                                        record.pricePerLiter
                                                    )
                                                }/L"
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                if (!dashboard.recentExpenses.isNullOrEmpty()) {
                                    Card {
                                        Column(Modifier.padding(16.dp)) {
                                            Text("Últimos Gastos")
                                            Spacer(modifier = Modifier.height(8.dp))
                                            dashboard.recentExpenses.take(5).forEach { expense ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Column {
                                                        Text(expense.type)
                                                        Text(expense.description)
                                                    }
                                                    Text(
                                                        NumberFormat.getCurrencyInstance(Locale.getDefault())
                                                            .format(expense.amount)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                dashboardState.value.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                // 📌 ——————— FIN DE TU CÓDIGO ORIGINAL ———————
            }
        }
    }
}
