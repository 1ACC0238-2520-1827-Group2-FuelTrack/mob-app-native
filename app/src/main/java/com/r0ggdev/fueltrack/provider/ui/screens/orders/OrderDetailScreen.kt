package com.r0ggdev.fueltrack.provider.ui.screens.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.ui.viewmodel.OrderViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    orderId: String,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(orderId) {
        viewModel.loadOrderById(orderId)
    }

    val order = uiState.selectedOrder

    // Estados para el dropdown
    var selectedStatus by remember(order) { mutableStateOf(order?.status ?: 0) }
    var expanded by remember { mutableStateOf(false) }

    val statusOptions = listOf(
        0 to "Pending",
        1 to "Assigned",
        2 to "En Route",
        3 to "Delivered",
        4 to "Cancelled"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Use default background color
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar - siempre visible
        TopAppBar(
            title = { Text("Detalle del Pedido") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
            }
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (order != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sección Superior: Información del Pedido
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Información del Pedido",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Pedido #${order.orderNumber}")
                                Text("Cliente: ${order.customerName ?: "No especificado"}")
                                Text("Terminal: ${order.deliveryAddress}")
                                Text(
                                    "Monto: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(order.totalAmount)}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            StatusBadge(status = order.status)
                        }
                    }
                }

                // Sección Media: Actualizar Estado
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Actualizar Estado del Pedido",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Dropdown para seleccionar estado
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = statusOptions.find { it.first == selectedStatus }?.second ?: "Seleccionar",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Nuevo Estado") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                statusOptions.forEach { (status, label) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = {
                                            selectedStatus = status
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                // TEMPORAL: Solo mostrar mensaje
                                println("Estado actualizado a: ${statusOptions.find { it.first == selectedStatus }?.second}")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Actualizar Estado")
                        }
                    }
                }

                // Sección Inferior: Acciones Rápidas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { /* Acción aprobar */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Aprobar Pedido")
                    }

                    Button(
                        onClick = { /* Acción despachar */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Marcar como\ndespachado")
                    }
                }

                Button(
                    onClick = { /* Acción cancelar */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cancelar Pedido")
                }
            }
        } else {
            // Error: Pedido no encontrado o error de carga
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val errorMessage = uiState.error ?: "Pedido no encontrado"
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
