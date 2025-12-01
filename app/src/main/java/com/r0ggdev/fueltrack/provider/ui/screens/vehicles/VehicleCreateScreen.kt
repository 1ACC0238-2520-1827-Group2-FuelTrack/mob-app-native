package com.r0ggdev.fueltrack.provider.ui.screens.vehicles

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.data.dto.CreateVehicleRequest
import com.r0ggdev.fueltrack.provider.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleCreateScreen(
    navController: NavController,
    viewModel: VehicleViewModel = hiltViewModel()
) {
    var licensePlate by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear Vehículo") })
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
                value = licensePlate,
                onValueChange = { licensePlate = it },
                label = { Text("Placa") },
                modifier = Modifier.fillMaxWidth()
            )

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
                value = year,
                onValueChange = { year = it },
                label = { Text("Año") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text("Capacidad (L)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    println("DEBUG: VehicleCreateScreen - Create button clicked")
                    println("DEBUG: VehicleCreateScreen - Form data: $licensePlate, $brand, $model, $year, $capacity")

                    val yearInt = year.toIntOrNull()
                    val capacityDouble = capacity.toDoubleOrNull()

                    println("DEBUG: VehicleCreateScreen - Parsed values: year=$yearInt, capacity=$capacityDouble")

                    if (yearInt != null && capacityDouble != null) {
                        println("DEBUG: VehicleCreateScreen - ✅ Validation passed, calling createVehicle")
                        viewModel.createVehicle(
                            CreateVehicleRequest(
                                licensePlate = licensePlate,
                                brand = brand,
                                model = model,
                                year = yearInt,
                                capacity = capacityDouble
                            )
                        ) {
                            println("DEBUG: VehicleCreateScreen - ✅ Vehicle created successfully, navigating back")
                            navController.popBackStack()
                        }
                    } else {
                        println("DEBUG: VehicleCreateScreen - ❌ Validation failed: year=$yearInt, capacity=$capacityDouble")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && licensePlate.isNotBlank() &&
                         brand.isNotBlank() && model.isNotBlank() &&
                         year.isNotBlank() && capacity.isNotBlank() &&
                         year.toIntOrNull() != null && capacity.toDoubleOrNull() != null
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Crear")
                }
            }
        }
    }
}
