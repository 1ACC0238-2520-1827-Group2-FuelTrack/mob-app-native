package com.r0ggdev.fueltrack.provider.ui.screens.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.r0ggdev.fueltrack.provider.ui.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDetailScreen(
    navController: NavController,
    notificationId: String,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(notificationId) {
        viewModel.loadNotificationById(notificationId)
        if (uiState.selectedNotification?.isRead == false) {
            viewModel.markAsRead(notificationId)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Notificación") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
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
            uiState.selectedNotification?.let { notification ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                notification.title,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(notification.message)
                            notification.relatedOrderNumber?.let {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Orden relacionada: $it")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Fecha: ${notification.createdAt}")
                        }
                    }
                }
            }
        }
    }
}

