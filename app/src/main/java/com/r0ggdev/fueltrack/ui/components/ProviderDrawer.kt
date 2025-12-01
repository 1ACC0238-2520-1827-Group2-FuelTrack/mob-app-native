package com.r0ggdev.fueltrack.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.background


@Composable
fun NavigationDrawer(
    userName: String,
    userEmail: String,
    userRole: String?,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C3F6E))   // azul
            .padding(start = 24.dp, top = 40.dp, bottom = 20.dp)
    ){

        // LOGO
        Text(
            text = "FuelTrack",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF2A64F6),
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.height(20.dp))

        // USER INFO
        Column(
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.7f)
                )
            )
        }

        Spacer(Modifier.height(10.dp))

        DrawerItem(
            icon = Icons.Default.Person,
            label = "Perfil",
            onClick = { onNavigate("profile") }
        )

        // Solo mostrar Order Management para proveedores
        if (userRole == "provider" || userRole == "PROVIDER") {
            DrawerItem(
                icon = Icons.Default.List,
                label = "Order Management",
                onClick = { onNavigate("orders") }
            )
        }
        //DrawerItem(Icons.Default.Check, "Conciliations")
        //DrawerItem(Icons.Default.Send, "Dispatch")
        //DrawerItem(Icons.Default.Notifications, "Analytics") // usando un ícono válido
        //DrawerItem(Icons.Default.Notifications, "Notifications")
        ////DrawerItem(Icons.Default.AttachMoney, "Prices")
        ////DrawerItem(Icons.Default.People, "Clients")
        //DrawerItem(Icons.Default.Email, "Contact us")

        Spacer(Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .clickable { onLogout() }
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(Modifier.width(10.dp))
            Text(
                "Log Out",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

