package com.r0ggdev.fueltrack.ui.screens.fuel

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
import com.r0ggdev.fueltrack.ui.viewmodel.FuelRecordViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelRecordDetailScreen(
    navController: NavController,
    recordId: String,
    viewModel: FuelRecordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val record = uiState.fuelRecords.find { it.id == recordId }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Registro") },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.FuelRecordEdit.createRoute(recordId)) }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(
                        onClick = {
                            viewModel.deleteFuelRecord(recordId) {
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
        if (record != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Fecha: ${record.date}", style = MaterialTheme.typography.bodyLarge)
                        Text("Kilometraje: ${record.odometer} km", style = MaterialTheme.typography.bodyLarge)
                        Text("Litros: ${record.liters} L", style = MaterialTheme.typography.bodyLarge)
                        Text("Precio: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(record.pricePerLiter)}/L", style = MaterialTheme.typography.bodyLarge)
                        Text("Total: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(record.liters * record.pricePerLiter)}", style = MaterialTheme.typography.bodyLarge)
                        Text("Tanque lleno: ${if (record.fullTank) "Sí" else "No"}", style = MaterialTheme.typography.bodyLarge)
                        record.gasStation?.let {
                            Text("Gasolinera: $it", style = MaterialTheme.typography.bodyLarge)
                        }
                        record.notes?.let {
                            Text("Notas: $it", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

