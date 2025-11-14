package com.r0ggdev.fueltrack.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.navigation.Screen
import com.r0ggdev.fueltrack.ui.viewmodel.AuthViewModel
import com.r0ggdev.fueltrack.ui.viewmodel.DashboardViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val dashboardState by dashboardViewModel.uiState.collectAsState()
    val authState by authViewModel.uiState.collectAsState()
    
    LaunchedEffect(authState.userId) {
        authState.userId?.let { userId ->
            dashboardViewModel.loadVehicles(userId)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    TextButton(
                        onClick = { navController.navigate(Screen.VehicleList.route) }
                    ) {
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        dashboardState.selectedVehicleId?.let { vehicleId ->
                            navController.navigate(Screen.FuelRecordCreate.createRoute(vehicleId))
                        }
                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar combustible")
                }
                FloatingActionButton(
                    onClick = {
                        dashboardState.selectedVehicleId?.let { vehicleId ->
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
            // Selector de vehículo
            if (dashboardState.vehicles.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = dashboardState.vehicles.find { it.id == dashboardState.selectedVehicleId }?.let {
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
                        dashboardState.vehicles.forEach { vehicle ->
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
            
            if (dashboardState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                dashboardState.dashboard?.let { dashboard ->
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // KPI Cards
                        item {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Card(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            "Consumo Promedio",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            dashboard.averageConsumption?.let {
                                                String.format("%.2f L/100km", it)
                                            } ?: "N/A",
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                    }
                                }
                                Card(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            "Costo Mensual",
                                            style = MaterialTheme.typography.bodySmall
                                        )
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
                        
                        // Último registro
                        item {
                            dashboard.lastFuelRecord?.let { record ->
                                Card {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            "Último Registro",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Fecha: ${record.date}")
                                        Text("Kilometraje: ${record.odometer} km")
                                        Text("Litros: ${record.liters} L")
                                        Text("Precio: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(record.pricePerLiter)}/L")
                                    }
                                }
                            }
                        }
                        
                        // Últimos gastos
                        item {
                            if (!dashboard.recentExpenses.isNullOrEmpty()) {
                                Card {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            "Últimos Gastos",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        dashboard.recentExpenses.take(5).forEach { expense ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column {
                                                    Text(expense.type, style = MaterialTheme.typography.bodyMedium)
                                                    Text(expense.description, style = MaterialTheme.typography.bodySmall)
                                                }
                                                Text(
                                                    NumberFormat.getCurrencyInstance(Locale.getDefault()).format(expense.amount),
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                } ?: run {
                    if (dashboardState.vehicles.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("No tienes vehículos registrados")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.navigate(Screen.VehicleCreate.route) }
                            ) {
                                Text("Agregar Vehículo")
                            }
                        }
                    }
                }
            }
            
            dashboardState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

