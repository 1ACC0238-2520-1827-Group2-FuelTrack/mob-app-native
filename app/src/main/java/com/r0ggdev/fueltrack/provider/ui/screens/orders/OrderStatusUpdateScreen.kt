package com.r0ggdev.fueltrack.provider.ui.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.ui.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderStatusUpdateScreen(
    navController: NavController,
    orderId: String,
    viewModel: OrderViewModel = hiltViewModel()
) {
    var selectedStatus by remember { mutableStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()
    
    val statusOptions = listOf(
        0 to "Pending",
        1 to "Assigned",
        2 to "En Route",
        3 to "Delivered",
        4 to "Cancelled"
    )
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Actualizar Estado") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Selecciona el nuevo estado:", style = MaterialTheme.typography.titleLarge)
            
            statusOptions.forEach { (status, label) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(label)
                    RadioButton(
                        selected = selectedStatus == status,
                        onClick = { selectedStatus = status }
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    viewModel.updateOrderStatus(orderId, selectedStatus) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Confirmar")
                }
            }
        }
    }
}

