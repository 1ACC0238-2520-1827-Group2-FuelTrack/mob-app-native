package com.r0ggdev.fueltrack.provider.ui.screens.vehicles

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen(
    navController: NavController,
    vehicleId: String,
    vehicleViewModel: VehicleViewModel = hiltViewModel()
) {
    LaunchedEffect(vehicleId) {
        vehicleViewModel.loadVehicleById(vehicleId)
    }

    val state by vehicleViewModel.uiState.collectAsState()
    val vehicle = state.selectedVehicle

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Vehículo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("vehicle_edit/$vehicleId") }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                }
            )
        }
    ) { paddingValues ->

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            vehicle != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Marca: ${vehicle.brand}")
                            Text("Modelo: ${vehicle.model}")
                            Text("Placa: ${vehicle.licensePlate}")
                            Text("Año: ${vehicle.year}")
                            Text("Capacidad del tanque: ${vehicle.capacity} L")
                        }
                    }
                }
            }
        }
    }
}
