package com.r0ggdev.fueltrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.r0ggdev.fueltrack.navigation.NavGraph
import com.r0ggdev.fueltrack.navigation.Screen
import com.r0ggdev.fueltrack.ui.theme.FuelTrackTheme
import com.r0ggdev.fueltrack.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FuelTrackTheme {
                FuelTrackApp()
            }
        }
    }
}

@Composable
fun FuelTrackApp(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.uiState.collectAsState()
    
    // Iniciar siempre en Splash, que decidirá la navegación
    NavGraph(
        navController = navController,
        startDestination = Screen.Splash.route
    )
}