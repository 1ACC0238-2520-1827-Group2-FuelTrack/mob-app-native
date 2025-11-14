package com.r0ggdev.fueltrack.provider.ui.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.navigation.ProviderScreen
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
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Orden") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            uiState.selectedOrder?.let { order ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Orden #${order.orderNumber}", style = MaterialTheme.typography.headlineMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Tipo de combustible: ${order.fuelType}")
                            Text("Cantidad: ${order.quantity} L")
                            Text("Precio por litro: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(order.pricePerLiter)}")
                            Text("Total: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(order.totalAmount)}")
                            StatusBadge(status = order.status)
                        }
                    }
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Dirección de entrega", style = MaterialTheme.typography.titleLarge)
                            Text(order.deliveryAddress)
                            order.deliveryLatitude?.let { lat ->
                                order.deliveryLongitude?.let { lon ->
                                    Text("Coordenadas: $lat, $lon")
                                }
                            }
                        }
                    }
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Asignación", style = MaterialTheme.typography.titleLarge)
                            order.assignedVehiclePlate?.let {
                                Text("Vehículo: $it")
                            }
                            order.assignedOperatorName?.let {
                                Text("Operador: $it")
                            }
                        }
                    }
                    
                    order.customerName?.let {
                        Text("Cliente: $it")
                    }
                    
                    order.estimatedDeliveryTime?.let {
                        Text("Entrega estimada: $it")
                    }
                    
                    order.actualDeliveryTime?.let {
                        Text("Entrega real: $it")
                    }
                    
                    Button(
                        onClick = {
                            navController.navigate(ProviderScreen.OrderStatusUpdate.createRoute(orderId))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Actualizar Estado")
                    }
                }
            }
        }
    }
}

