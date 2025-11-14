package com.r0ggdev.fueltrack.provider.ui.screens.operators

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.data.dto.UpdateOperatorRequest
import com.r0ggdev.fueltrack.provider.ui.viewmodel.OperatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorEditScreen(
    navController: NavController,
    operatorId: String,
    viewModel: OperatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }
    var licenseExpiryDate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }
    
    LaunchedEffect(operatorId) {
        viewModel.loadOperatorById(operatorId)
    }
    
    LaunchedEffect(uiState.selectedOperator) {
        uiState.selectedOperator?.let { operator ->
            firstName = operator.firstName
            lastName = operator.lastName
            licenseNumber = operator.licenseNumber
            licenseExpiryDate = operator.licenseExpiryDate
            phone = operator.phone
            isActive = operator.isActive
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Editar Operador") })
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
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = licenseNumber,
                onValueChange = { licenseNumber = it },
                label = { Text("Número de Licencia") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = licenseExpiryDate,
                onValueChange = { licenseExpiryDate = it },
                label = { Text("Fecha de Expiración") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text("Activo")
                Switch(checked = isActive, onCheckedChange = { isActive = it })
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    viewModel.updateOperator(
                        operatorId,
                        UpdateOperatorRequest(
                            firstName = firstName,
                            lastName = lastName,
                            licenseNumber = licenseNumber,
                            licenseExpiryDate = licenseExpiryDate,
                            phone = phone,
                            isActive = isActive
                        )
                    ) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}

