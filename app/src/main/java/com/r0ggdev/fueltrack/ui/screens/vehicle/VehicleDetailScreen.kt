package com.r0ggdev.fueltrack.ui.screens.vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.navigation.Screen
import com.r0ggdev.fueltrack.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen(
    navController: NavController,
    vehicleId: String,
    vehicleViewModel: VehicleViewModel = hiltViewModel()
) {
    val vehicleState by vehicleViewModel.uiState.collectAsState()
    val vehicle = vehicleState.vehicles.find { it.id == vehicleId }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Vehículo") },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.VehicleEdit.createRoute(vehicleId)) }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(
                        onClick = {
                            vehicleViewModel.deleteVehicle(vehicleId) {
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (vehicle != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Marca: ${vehicle.brand}", style = MaterialTheme.typography.bodyLarge)
                        Text("Modelo: ${vehicle.model}", style = MaterialTheme.typography.bodyLarge)
                        Text("Placa: ${vehicle.plate}", style = MaterialTheme.typography.bodyLarge)
                        Text("Año: ${vehicle.year}", style = MaterialTheme.typography.bodyLarge)
                        Text("Capacidad: ${vehicle.tankCapacity} L", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                
                Button(
                    onClick = { navController.navigate(Screen.FuelRecordList.createRoute(vehicleId)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Registros de Combustible")
                }
                
                Button(
                    onClick = { navController.navigate(Screen.ExpenseList.createRoute(vehicleId)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Gastos")
                }
            }
        }
    }
}

