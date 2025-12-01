package com.r0ggdev.fueltrack.provider.ui.screens.vehicles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.navigation.ProviderScreen
import com.r0ggdev.fueltrack.provider.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleListScreen(
    navController: NavController,
    viewModel: VehicleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadVehicles()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Flota") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ProviderScreen.VehicleCreate.route) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar vehículo")
            }
        }
    ) { paddingValues ->

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null && uiState.vehicles.isEmpty() -> {
                // Solo mostrar error si no hay datos para mostrar
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error al cargar vehículos",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadVehicles() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            uiState.vehicles.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay vehículos registrados")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Header informativo
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = "Información",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${uiState.vehicles.size} vehículos cargados desde el backend",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(uiState.vehicles) { vehicle ->
                        VehicleCard(
                            vehicle = vehicle,
                            onClick = {
                                navController.navigate(
                                    ProviderScreen.VehicleDetail.createRoute(vehicle.id)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VehicleCard(
    vehicle: com.r0ggdev.fueltrack.provider.data.dto.ProviderVehicleDto,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = vehicle.licensePlate,
                style = MaterialTheme.typography.titleLarge
            )

            Text("${vehicle.brand} ${vehicle.model} (${vehicle.year})")

            Text("Capacidad: ${vehicle.capacity} L")

            Text("Estado: ${vehicle.status}")

            if (vehicle.currentLatitude != null && vehicle.currentLongitude != null) {
                Text("Ubicación: ${vehicle.currentLatitude}, ${vehicle.currentLongitude}")
            }
        }
    }
}
