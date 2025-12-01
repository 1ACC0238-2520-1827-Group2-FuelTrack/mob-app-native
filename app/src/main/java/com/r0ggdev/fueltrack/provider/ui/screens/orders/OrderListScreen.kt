package com.r0ggdev.fueltrack.provider.ui.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
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
    viewModel: OrderViewModel = hiltViewModel(),
    authViewModel: com.r0ggdev.fueltrack.ui.viewmodel.AuthViewModel = hiltViewModel()
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val uiState by viewModel.uiState.collectAsState()
    val authState by authViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        println("DEBUG: OrderListScreen - ==========================================")
        println("DEBUG: OrderListScreen - 🔍 CHECKING USER AUTHENTICATION & ROLE")
        println("DEBUG: OrderListScreen - User authenticated: ${authState.isAuthenticated}")
        println("DEBUG: OrderListScreen - User role: '${authState.role}'")
        println("DEBUG: OrderListScreen - User role uppercase: '${authState.role?.uppercase()}'")
        println("DEBUG: OrderListScreen - User email: ${authState.email}")
        println("DEBUG: OrderListScreen - User ID: ${authState.userId}")
        println("DEBUG: OrderListScreen - ==========================================")

        if (!authState.isAuthenticated) {
            println("DEBUG: OrderListScreen - ❌ USER NOT AUTHENTICATED!")
            println("DEBUG: OrderListScreen - This will cause 401 Unauthorized")
        } else if (authState.role?.uppercase() != "PROVIDER") {
            println("DEBUG: OrderListScreen - 🚫 USER ROLE IS '${authState.role}' - NOT 'PROVIDER'")
            println("DEBUG: OrderListScreen - This WILL cause 403 Forbidden error")
            println("DEBUG: OrderListScreen - The backend requires PROVIDER role for /api/Orders/provider")
            println("DEBUG: OrderListScreen - 💡 SOLUTION: Change user role to 'PROVIDER' in backend/database")
        } else {
            println("DEBUG: OrderListScreen - ✅ USER IS AUTHENTICATED AS PROVIDER")
            println("DEBUG: OrderListScreen - Should be able to access orders...")
        }

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
            // User info banner for debugging
            if (authState.role?.uppercase() != "PROVIDER") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Advertencia",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Rol actual: '${authState.role}' - Necesitas rol PROVIDER para ver órdenes",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
            // Filter Chips - LazyRow para scroll horizontal
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(listOf("All", "Pending", "Assigned", "En Route", "Delivered", "Cancelled")) { filter ->
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
                // Mapear nombres de filtro a valores numéricos de estado
                val statusMap = mapOf(
                    "Pending" to 0,
                    "Assigned" to 1,
                    "En Route" to 2,
                    "Delivered" to 3,
                    "Cancelled" to 4
                )

                val filteredOrders = if (selectedFilter == "All") {
                    uiState.orders
                } else {
                    val targetStatus = statusMap[selectedFilter]
                    if (targetStatus != null) {
                        uiState.orders.filter { order ->
                            order.status == targetStatus
                        }
                    } else {
                        uiState.orders // Fallback si el filtro no existe
                    }
                }
                
                if (filteredOrders.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val errorMessage = uiState.error
                        if (errorMessage != null) {
                            // Mostrar error
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Error al cargar órdenes",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = errorMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { viewModel.loadOrders() }) {
                                    Text("Reintentar")
                                }
                            }
                        } else {
                            // Solo mostrar "No hay órdenes" si no hay error
                            Text("No hay órdenes")
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Header informativo - CONFIRMA DATOS REALES
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (uiState.hasRealData) {
                                        MaterialTheme.colorScheme.primaryContainer // Verde/azul para datos reales
                                    } else {
                                        MaterialTheme.colorScheme.secondaryContainer // Gris para datos mock
                                    }
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        if (uiState.hasRealData) Icons.Default.CheckCircle else Icons.Default.Info,
                                        contentDescription = "Estado de datos",
                                        tint = if (uiState.hasRealData) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSecondaryContainer
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (uiState.hasRealData) {
                                                "✅ ÓRDENES REALES DEL BACKEND"
                                            } else {
                                                "⚠️ DATOS DE DEMOSTRACIÓN"
                                            },
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                            color = if (uiState.hasRealData) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.onSecondaryContainer
                                            }
                                        )
                                        Text(
                                            text = "${uiState.orders.size} órdenes cargadas",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (uiState.hasRealData) {
                                                MaterialTheme.colorScheme.onPrimaryContainer
                                            } else {
                                                MaterialTheme.colorScheme.onSecondaryContainer
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

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
                    "ID: ${order.orderNumber}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                StatusBadge(status = order.status)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Fecha: ${order.createdAt.take(10)}", style = MaterialTheme.typography.bodyMedium)
                    Text("Terminal: ${order.deliveryAddress}", style = MaterialTheme.typography.bodyMedium)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = androidx.compose.ui.Alignment.End) {
                    Text(
                        "Monto: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(order.totalAmount)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
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

