package com.r0ggdev.fueltrack.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.navigation.Screen
import com.r0ggdev.fueltrack.ui.viewmodel.AuthViewModel
import java.util.regex.Pattern

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.isAuthenticated, uiState.role) {
        if (uiState.isAuthenticated && uiState.role != null) {
            // Navigate based on user role
            val userRole = uiState.role!!
            println("DEBUG: LoginScreen - Navigation decision:")
            println("DEBUG: LoginScreen - userRole: '$userRole'")
            println("DEBUG: LoginScreen - userRole.equals('Proveedor', ignoreCase=true): ${userRole.equals("Proveedor", ignoreCase = true)}")
            println("DEBUG: LoginScreen - userRole.equals('Administrador', ignoreCase=true): ${userRole.equals("Administrador", ignoreCase = true)}")

            when {
                // Proveedor - verificar exactamente "Proveedor" (con P mayúscula como devuelve el backend)
                userRole.equals("Proveedor", ignoreCase = true) -> {
                    println("DEBUG: LoginScreen - ✅ User role 'Proveedor', navigating to provider_dashboard")
                    navController.navigate("provider_dashboard") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
                // Administrador - va al dashboard regular (cliente)
                userRole.equals("Administrador", ignoreCase = true) -> {
                    println("DEBUG: LoginScreen - ✅ User role 'Administrador', navigating to regular dashboard")
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
                else -> {
                    println("DEBUG: LoginScreen - ❌ User role '$userRole' not recognized, navigating to regular dashboard")
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "FuelTrack",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError != null,
            supportingText = emailError?.let { { Text(it) } }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = passwordError?.let { { Text(it) } }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                var isValid = true
                
                // Validar email
                val emailPattern = Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
                )
                if (email.isBlank() || !emailPattern.matcher(email).matches()) {
                    emailError = "Email inválido"
                    isValid = false
                }
                
                // Validar password
                if (password.isBlank()) {
                    passwordError = "La contraseña no puede estar vacía"
                    isValid = false
                }
                
                if (isValid) {
                    viewModel.login(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Iniciar Sesión")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(
            onClick = { navController.navigate(Screen.Signup.route) }
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
        
        uiState.error?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

