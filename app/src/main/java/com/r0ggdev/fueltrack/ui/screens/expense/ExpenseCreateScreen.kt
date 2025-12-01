package com.r0ggdev.fueltrack.ui.screens.expense

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.data.dto.CreateExpenseRequest
import com.r0ggdev.fueltrack.ui.viewmodel.ExpenseViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseCreateScreen(
    navController: NavController,
    vehicleId: String,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_DATE)) }
    
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Agregar Gasto") })
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
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Monto") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Tipo (Mantenimiento, Reparación, Seguro, etc.)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Fecha (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    
                    viewModel.createExpense(
                        CreateExpenseRequest(
                            vehicleId = vehicleId,
                            amount = amountDouble,
                            type = type,
                            description = description,
                            date = date
                        )
                    ) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && amount.isNotBlank() && 
                         type.isNotBlank() && description.isNotBlank() && date.isNotBlank()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Crear Gasto")
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

