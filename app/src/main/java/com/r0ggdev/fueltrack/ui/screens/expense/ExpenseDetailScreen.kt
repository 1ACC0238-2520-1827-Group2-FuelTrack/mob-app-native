package com.r0ggdev.fueltrack.ui.screens.expense

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
import com.r0ggdev.fueltrack.ui.viewmodel.ExpenseViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailScreen(
    navController: NavController,
    expenseId: String,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val expense = uiState.expenses.find { it.id == expenseId }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Gasto") },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.ExpenseEdit.createRoute(expenseId)) }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(
                        onClick = {
                            viewModel.deleteExpense(expenseId) {
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
        if (expense != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Tipo: ${expense.type}", style = MaterialTheme.typography.bodyLarge)
                        Text("Descripción: ${expense.description}", style = MaterialTheme.typography.bodyLarge)
                        Text("Fecha: ${expense.date}", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "Monto: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(expense.amount)}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

