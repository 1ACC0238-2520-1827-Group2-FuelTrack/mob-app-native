package com.r0ggdev.fueltrack.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.navigation.Screen
import com.r0ggdev.fueltrack.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        delay(2000) // Esperar 2 segundos para mostrar splash

        println("DEBUG: SplashScreen - uiState.isAuthenticated: ${uiState.isAuthenticated}")
        println("DEBUG: SplashScreen - uiState.role: '${uiState.role}'")
        println("DEBUG: SplashScreen - uiState.role?.length: ${uiState.role?.length}")
        println("DEBUG: SplashScreen - uiState.role?.uppercase(): '${uiState.role?.uppercase()}'")

        if (uiState.isAuthenticated) {
            // Usuario autenticado - navegar según rol
            val userRole = uiState.role
            println("DEBUG: SplashScreen - userRole: '$userRole'")
            println("DEBUG: SplashScreen - userRole.equals('Proveedor', ignoreCase=true): ${userRole?.equals("Proveedor", ignoreCase = true)}")
            println("DEBUG: SplashScreen - userRole.equals('Administrador', ignoreCase=true): ${userRole?.equals("Administrador", ignoreCase = true)}")

            when {
                // Proveedor - verificar exactamente "Proveedor" (con P mayúscula como devuelve el backend)
                userRole?.equals("Proveedor", ignoreCase = true) == true -> {
                    println("DEBUG: SplashScreen - ✅ User role 'Proveedor', navigating to provider_dashboard")
                    navController.navigate("provider_dashboard") {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
                // Administrador - va al dashboard regular (cliente)
                userRole?.equals("Administrador", ignoreCase = true) == true -> {
                    println("DEBUG: SplashScreen - ✅ User role 'Administrador', navigating to regular dashboard")
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
                else -> {
                    println("DEBUG: SplashScreen - ❌ User role '$userRole' not recognized, navigating to regular dashboard")
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            }
        } else {
            println("DEBUG: SplashScreen - ❌ User not authenticated, navigating to login")
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

