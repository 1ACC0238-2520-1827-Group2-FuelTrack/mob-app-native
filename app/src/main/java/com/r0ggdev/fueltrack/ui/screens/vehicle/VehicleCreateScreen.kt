package com.r0ggdev.fueltrack.ui.screens.vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.data.dto.CreateVehicleRequest
import com.r0ggdev.fueltrack.ui.viewmodel.AuthViewModel
import com.r0ggdev.fueltrack.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleCreateScreen(
    navController: NavController,
    vehicleViewModel: VehicleViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }

    val vehicleState by vehicleViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Agregar Vehículo") })
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
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text("Capacidad del Tanque (L)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val yearInt = year.toIntOrNull() ?: 0
                    val capacityInt = capacity.toIntOrNull() ?: 0

                    vehicleViewModel.createVehicle(
                        CreateVehicleRequest(
                            licensePlate = plate,
                            brand = brand,
                            model = model,
                            year = yearInt,
                            capacity = capacityInt
                        )
                    ) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !vehicleState.isLoading &&
                        brand.isNotBlank() &&
                        model.isNotBlank() &&
                        plate.isNotBlank() &&
                        year.isNotBlank() &&
                        capacity.isNotBlank()
            ) {
                if (vehicleState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Crear Vehículo")
                }
            }

            vehicleState.error?.let { error ->
                Text(
                    text = "Error al crear vehículo: $error",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
