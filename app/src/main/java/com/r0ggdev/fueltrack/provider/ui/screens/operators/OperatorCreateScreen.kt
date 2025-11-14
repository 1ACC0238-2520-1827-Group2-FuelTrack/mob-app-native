package com.r0ggdev.fueltrack.provider.ui.screens.operators

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.data.dto.CreateOperatorRequest
import com.r0ggdev.fueltrack.provider.ui.viewmodel.OperatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorCreateScreen(
    navController: NavController,
    viewModel: OperatorViewModel = hiltViewModel()
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }
    var licenseExpiryDate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear Operador") })
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
                label = { Text("Fecha de Expiración (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    viewModel.createOperator(
                        CreateOperatorRequest(
                            firstName = firstName,
                            lastName = lastName,
                            licenseNumber = licenseNumber,
                            licenseExpiryDate = licenseExpiryDate,
                            phone = phone
                        )
                    ) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && firstName.isNotBlank() && 
                         lastName.isNotBlank() && licenseNumber.isNotBlank() && 
                         licenseExpiryDate.isNotBlank() && phone.isNotBlank()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Crear")
                }
            }
        }
    }
}

