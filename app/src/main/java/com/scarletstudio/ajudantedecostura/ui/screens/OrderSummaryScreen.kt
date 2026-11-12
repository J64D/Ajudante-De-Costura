package com.scarletstudio.ajudantedecostura.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.data.local.PieceDatabase
import com.scarletstudio.ajudantedecostura.navigation.Routes
import com.scarletstudio.ajudantedecostura.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun OrderSummaryScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dao = remember { PieceDatabase.getDatabase(context).pieceDao() }
    val pieces by dao.getAll().collectAsState(initial = emptyList())
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val total = pieces.sumOf { it.quantity.toLong() }
    Scaffold(topBar = { AtelierTopBar("Resumo das peças", { navController.popBackStack() }) }, snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (pieces.isNotEmpty()) item { InlineAd() }
            item { LiquidHero("SEU ATELIÊ EM NÚMEROS", "$total peças", "Resumo de todos os registros cadastrados.") }

            items(pieces, key = { it.id }) { piece ->
                Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(18.dp)) {
                    Text(piece.name, style = MaterialTheme.typography.titleMedium)
                    Text("${piece.color} • ${piece.size} • ${piece.quantity} unidade(s)")
                } }
            }
            if (pieces.isEmpty()) item { Text("Adicione uma peça para começar seu resumo.") }
            item { Button(enabled = pieces.isNotEmpty(), onClick = {
                val message = buildString {
                    append("Meu ateliê — resumo das peças\n\n")
                    pieces.forEach { append("• ${it.name} | ${it.color} | ${it.size}: ${it.quantity}\n") }
                    append("\nTotal: $total peças")
                }
                runCatching { context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"; putExtra(Intent.EXTRA_TEXT, message)
                }, "Compartilhar resumo")) }.onFailure { scope.launch { snackbar.showSnackbar("Nenhum aplicativo disponível para compartilhar.") } }
            }, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)) { Icon(Icons.Rounded.IosShare, null); Spacer(Modifier.width(8.dp)); Text("Compartilhar resumo") } }
            item { OutlinedButton(onClick = { navController.navigate(Routes.ADD_PIECE) }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text("Adicionar outra peça") } }
        }
    }
}

