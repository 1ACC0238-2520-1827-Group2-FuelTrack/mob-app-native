package com.r0ggdev.fueltrack.provider.ui.screens.operators

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.navigation.ProviderScreen
import com.r0ggdev.fueltrack.provider.ui.viewmodel.OperatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorDetailScreen(
    navController: NavController,
    operatorId: String,
    viewModel: OperatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(operatorId) {
        viewModel.loadOperatorById(operatorId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Operador") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(ProviderScreen.OperatorEdit.createRoute(operatorId))
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = {
                        viewModel.deleteOperator(operatorId) {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
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
            uiState.selectedOperator?.let { operator ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Nombre: ${operator.firstName} ${operator.lastName}", style = MaterialTheme.typography.titleLarge)
                    Text("Licencia: ${operator.licenseNumber}")
                    Text("Expiración de Licencia: ${operator.licenseExpiryDate}")
                    Text("Teléfono: ${operator.phone}")
                    Text("Estado: ${if (operator.isActive) "Activo" else "Inactivo"}")
                }
            }
        }
    }
}
