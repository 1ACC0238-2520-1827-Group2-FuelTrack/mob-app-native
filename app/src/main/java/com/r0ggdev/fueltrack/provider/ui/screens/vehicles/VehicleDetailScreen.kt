package com.r0ggdev.fueltrack.provider.ui.screens.vehicles

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.navigation.ProviderScreen
import com.r0ggdev.fueltrack.provider.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen(
    navController: NavController,
    vehicleId: String,
    viewModel: VehicleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(vehicleId) {
        viewModel.loadVehicleById(vehicleId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Vehículo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            uiState.selectedVehicle?.let { vehicle ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Placa: ${vehicle.licensePlate}", style = MaterialTheme.typography.titleLarge)
                            Text("Marca: ${vehicle.brand}")
                            Text("Modelo: ${vehicle.model}")
                            Text("Año: ${vehicle.year}")
                            Text("Capacidad: ${vehicle.capacity} L")
                            Text("Estado: ${vehicle.status}")
                        }
                    }
                    
                    vehicle.currentLatitude?.let { lat ->
                        vehicle.currentLongitude?.let { lon ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.extraLarge
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Ubicación Actual", style = MaterialTheme.typography.titleLarge)
                                    Text("Latitud: $lat")
                                    Text("Longitud: $lon")
                                    // Aquí podrías agregar un mapa
                                }
                            }
                        }
                    }
                    
                    Button(
                        onClick = {
                            navController.navigate(ProviderScreen.VehicleLocationUpdate.createRoute(vehicleId))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Actualizar Ubicación")
                    }
                }
            }
        }
    }
}

