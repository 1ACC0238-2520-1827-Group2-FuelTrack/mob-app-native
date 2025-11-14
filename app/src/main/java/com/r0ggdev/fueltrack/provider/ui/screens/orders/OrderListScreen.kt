package com.r0ggdev.fueltrack.provider.ui.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun OrderListScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Órdenes") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Pending", "Assigned", "En Route", "Delivered", "Cancelled").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) }
                    )
                }
            }
            
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val filteredOrders = if (selectedFilter == "All") {
                    uiState.orders
                } else {
                    uiState.orders.filter { 
                        // Filtrar según el estado - esto es un placeholder
                        // Necesitarías mapear los estados del backend
                        true
                    }
                }
                
                if (filteredOrders.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay órdenes")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredOrders) { order ->
                            OrderCard(
                                order = order,
                                onClick = {
                                    navController.navigate(ProviderScreen.OrderDetail.createRoute(order.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: com.r0ggdev.fueltrack.provider.data.dto.OrderDto,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Orden #${order.orderNumber}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                StatusBadge(status = order.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Dirección: ${order.deliveryAddress}")
            Text("Cantidad: ${order.quantity} L")
            Text("Tipo: ${order.fuelType}")
            Text(
                "Total: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(order.totalAmount)}"
            )
            order.estimatedDeliveryTime?.let {
                Text("Entrega estimada: $it")
            }
        }
    }
}

@Composable
fun StatusBadge(status: Int) {
    val (text, color) = when (status) {
        0 -> "Pending" to MaterialTheme.colorScheme.tertiary
        1 -> "Assigned" to MaterialTheme.colorScheme.primary
        2 -> "En Route" to MaterialTheme.colorScheme.secondary
        3 -> "Delivered" to MaterialTheme.colorScheme.primaryContainer
        4 -> "Cancelled" to MaterialTheme.colorScheme.error
        else -> "Unknown" to MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

