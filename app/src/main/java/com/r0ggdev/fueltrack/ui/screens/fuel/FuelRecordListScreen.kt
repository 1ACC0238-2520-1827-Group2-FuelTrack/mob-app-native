package com.r0ggdev.fueltrack.ui.screens.fuel

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
import com.r0ggdev.fueltrack.ui.viewmodel.FuelRecordViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelRecordListScreen(
    navController: NavController,
    vehicleId: String,
    viewModel: FuelRecordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(vehicleId) {
        viewModel.loadFuelRecords(vehicleId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Registros de Combustible") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.FuelRecordCreate.createRoute(vehicleId)) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar registro")
            }
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
            if (uiState.fuelRecords.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No hay registros de combustible")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.fuelRecords) { record ->
                        Card(
                            onClick = { navController.navigate(Screen.FuelRecordDetail.createRoute(record.id)) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    "Fecha: ${record.date}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("Kilometraje: ${record.odometer} km")
                                Text("Litros: ${record.liters} L")
                                Text("Precio: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(record.pricePerLiter)}/L")
                                Text("Total: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(record.liters * record.pricePerLiter)}")
                                if (record.fullTank) {
                                    Text("Tanque lleno", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
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

