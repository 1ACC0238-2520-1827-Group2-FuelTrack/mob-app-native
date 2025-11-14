package com.r0ggdev.fueltrack.provider.ui.screens.vehicles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (uiState.vehicles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay vehículos")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.vehicles) { vehicle ->
                        VehicleCard(
                            vehicle = vehicle,
                            onClick = {
                                navController.navigate(ProviderScreen.VehicleDetail.createRoute(vehicle.id))
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
                vehicle.licensePlate,
                style = MaterialTheme.typography.titleLarge
            )
            Text("${vehicle.brand} ${vehicle.model} (${vehicle.year})")
            Text("Capacidad: ${vehicle.capacity} L")
            Text("Estado: ${vehicle.status}")
            vehicle.currentLatitude?.let { lat ->
                vehicle.currentLongitude?.let { lon ->
                    Text("Ubicación: $lat, $lon")
                }
            }
        }
    }
}

