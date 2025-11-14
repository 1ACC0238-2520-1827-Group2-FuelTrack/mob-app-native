package com.r0ggdev.fueltrack.provider.ui.screens.operators

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
import com.r0ggdev.fueltrack.provider.navigation.ProviderScreen
import com.r0ggdev.fueltrack.provider.ui.viewmodel.OperatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorListScreen(
    navController: NavController,
    viewModel: OperatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadOperators()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Operadores") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ProviderScreen.OperatorCreate.route) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar operador")
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
            if (uiState.operators.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay operadores")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.operators) { operator ->
                        OperatorCard(
                            operator = operator,
                            onClick = {
                                navController.navigate(ProviderScreen.OperatorDetail.createRoute(operator.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OperatorCard(
    operator: com.r0ggdev.fueltrack.provider.data.dto.OperatorDto,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "${operator.firstName} ${operator.lastName}",
                style = MaterialTheme.typography.titleLarge
            )
            Text("Teléfono: ${operator.phone}")
            Text("Licencia: ${operator.licenseNumber}")
            Text("Expira: ${operator.licenseExpiryDate}")
            Text("Estado: ${if (operator.isActive) "Activo" else "Inactivo"}")
        }
    }
}

