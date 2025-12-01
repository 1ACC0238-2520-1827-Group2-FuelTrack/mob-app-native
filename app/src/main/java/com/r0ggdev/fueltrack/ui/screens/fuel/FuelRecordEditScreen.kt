package com.r0ggdev.fueltrack.ui.screens.fuel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.data.dto.UpdateFuelRecordRequest
import com.r0ggdev.fueltrack.ui.viewmodel.FuelRecordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelRecordEditScreen(
    navController: NavController,
    recordId: String,
    viewModel: FuelRecordViewModel = hiltViewModel()
) {
    // En una implementación completa, cargarías el registro primero
    var odometer by remember { mutableStateOf("") }
    var liters by remember { mutableStateOf("") }
    var pricePerLiter by remember { mutableStateOf("") }
    var fullTank by remember { mutableStateOf(false) }
    var gasStation by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Editar Registro de Combustible") })
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
                value = odometer,
                onValueChange = { odometer = it },
                label = { Text("Kilometraje") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = liters,
                onValueChange = { liters = it },
                label = { Text("Litros") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = pricePerLiter,
                onValueChange = { pricePerLiter = it },
                label = { Text("Precio por Litro") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tanque lleno")
                Switch(
                    checked = fullTank,
                    onCheckedChange = { fullTank = it }
                )
            }
            
            OutlinedTextField(
                value = gasStation,
                onValueChange = { gasStation = it },
                label = { Text("Gasolinera (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notas (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    val odometerInt = odometer.toIntOrNull() ?: 0
                    val litersDouble = liters.toDoubleOrNull() ?: 0.0
                    val priceDouble = pricePerLiter.toDoubleOrNull() ?: 0.0
                    
                    viewModel.updateFuelRecord(
                        recordId,
                        UpdateFuelRecordRequest(
                            odometer = odometerInt,
                            liters = litersDouble,
                            pricePerLiter = priceDouble,
                            fullTank = fullTank,
                            gasStation = gasStation.takeIf { it.isNotBlank() },
                            notes = notes.takeIf { it.isNotBlank() }
                        )
                    ) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Guardar Cambios")
                }
            }
            
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

