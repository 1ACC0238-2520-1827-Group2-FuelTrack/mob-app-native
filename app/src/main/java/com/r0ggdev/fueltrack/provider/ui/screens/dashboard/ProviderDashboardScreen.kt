package com.r0ggdev.fueltrack.provider.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.navigation.ProviderScreen
import com.r0ggdev.fueltrack.provider.ui.viewmodel.DashboardViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadAnalytics()
    }
    
    var isRefreshing by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Dashboard", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "Bienvenido",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(ProviderScreen.Profile.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading && uiState.analytics == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // KPI Cards
                uiState.analytics?.let { analytics ->
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            KpiCard(
                                title = "Órdenes Totales",
                                value = analytics.totalOrders.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            KpiCard(
                                title = "Pendientes",
                                value = analytics.pendingOrders.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            KpiCard(
                                title = "Completadas",
                                value = analytics.completedOrders.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            KpiCard(
                                title = "Ingresos",
                                value = NumberFormat.getCurrencyInstance(Locale.getDefault())
                                    .format(analytics.totalRevenue),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            KpiCard(
                                title = "Vehículos Activos",
                                value = analytics.activeVehicles.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            KpiCard(
                                title = "Operadores Disponibles",
                                value = analytics.availableOperators.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    
                    // Monthly Revenue Chart Placeholder
                    if (analytics.monthlyRevenue.isNotEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.extraLarge
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        "Ingresos Mensuales",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    // Placeholder para gráfico - puedes usar una librería de charts aquí
                                    Text(
                                        "Gráfico de barras aquí",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    analytics.monthlyRevenue.take(6).forEach { revenue ->
                                        Text(
                                            "${revenue.month}: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(revenue.revenue)}",
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Fuel Type Stats
                    if (analytics.fuelTypeStats.isNotEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.extraLarge
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        "Distribución por Tipo de Combustible",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    analytics.fuelTypeStats.forEach { stat ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(stat.fuelType)
                                            Text("${stat.count} órdenes")
                                            Text(
                                                NumberFormat.getCurrencyInstance(Locale.getDefault())
                                                    .format(stat.totalAmount),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    item {
                        Button(
                            onClick = { navController.navigate(ProviderScreen.Orders.route) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver Todas las Órdenes")
                        }
                    }
                }
                
                uiState.error?.let { error ->
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = error,
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

