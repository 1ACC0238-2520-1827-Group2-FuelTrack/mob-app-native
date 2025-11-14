package com.r0ggdev.fueltrack.ui.screens.vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.navigation.Screen
import com.r0ggdev.fueltrack.ui.viewmodel.AuthViewModel
import com.r0ggdev.fueltrack.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleListScreen(
    navController: NavController,
    vehicleViewModel: VehicleViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val vehicleState by vehicleViewModel.uiState.collectAsState()
    val authState by authViewModel.uiState.collectAsState()
    
    LaunchedEffect(authState.userId) {
        authState.userId?.let { userId ->
            vehicleViewModel.loadVehicles(userId)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Vehículos") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.VehicleCreate.route) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar vehículo")
            }
        }
    ) { paddingValues ->
        if (vehicleState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (vehicleState.vehicles.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No hay vehículos registrados")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vehicleState.vehicles) { vehicle ->
                        Card(
                            onClick = { navController.navigate(Screen.VehicleDetail.createRoute(vehicle.id)) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    "${vehicle.brand} ${vehicle.model}",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    "Placa: ${vehicle.plate}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "Año: ${vehicle.year}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    "Capacidad: ${vehicle.tankCapacity} L",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
        
        vehicleState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

