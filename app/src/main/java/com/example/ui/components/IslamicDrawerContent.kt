package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IslamicDrawerContent(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Menu", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        NavigationDrawerItem(
            label = { Text("Home") },
            selected = false,
            onClick = { onNavigate("home") }
        )
        // Add more items here
    }
}
