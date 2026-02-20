package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExportScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Exportar Peças", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { /* exportar JSON */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Exportar")
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = onBack) {
            Text("Voltar")
        }
    }
}
