package com.r0ggdev.fueltrack.ui.screens.vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.data.dto.UpdateVehicleRequest
import com.r0ggdev.fueltrack.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleEditScreen(
    navController: NavController,
    vehicleId: String,
    vehicleViewModel: VehicleViewModel = hiltViewModel()
) {
    // En una implementación completa, cargarías el vehículo primero
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var tankCapacity by remember { mutableStateOf("") }
    
    val vehicleState by vehicleViewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Editar Vehículo") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Modelo") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = plate,
                onValueChange = { plate = it },
                label = { Text("Placa") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Año") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = tankCapacity,
                onValueChange = { tankCapacity = it },
                label = { Text("Capacidad del Tanque (L)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    val yearInt = year.toIntOrNull() ?: 0
                    val tankCapacityDouble = tankCapacity.toDoubleOrNull() ?: 0.0
                    
                    vehicleViewModel.updateVehicle(
                        vehicleId,
                        UpdateVehicleRequest(
                            brand = brand,
                            model = model,
                            plate = plate,
                            year = yearInt,
                            tankCapacity = tankCapacityDouble
                        )
                    ) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !vehicleState.isLoading
            ) {
                if (vehicleState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Guardar Cambios")
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
}

