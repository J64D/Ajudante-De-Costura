package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.data.local.PieceDatabase
import com.scarletstudio.ajudantedecostura.data.local.entity.PieceEntity
import com.scarletstudio.ajudantedecostura.navigation.Routes
import com.scarletstudio.ajudantedecostura.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dao = remember { PieceDatabase.getDatabase(context).pieceDao() }
    val pieces by dao.getAll().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    var query by rememberSaveable { mutableStateOf("") }
    var deleting by remember { mutableStateOf<PieceEntity?>(null) }
    val filtered = pieces.filter { listOf(it.name, it.color, it.size).any { value -> value.contains(query, true) } }
    Scaffold(topBar = { AtelierTopBar("Minhas peças", { navController.popBackStack() }) {
        IconButton(onClick = { navController.navigate(Routes.ORDER_SUMMARY) }) { Icon(Icons.Rounded.IosShare, "Resumo e compartilhamento") }
    } }, snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = { ExtendedFloatingActionButton(onClick = { navController.navigate(Routes.ADD_PIECE) },
            icon = { Icon(Icons.Rounded.Add, null) }, text = { Text("Nova peça") }) }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)) {
            if (filtered.isNotEmpty()) item { InlineAd() }
            item { Text("${pieces.sumOf { it.quantity.toLong() }} peças no seu ateliê", style = MaterialTheme.typography.titleLarge) }
            item { OutlinedTextField(query, { query = it }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                label = { Text("Buscar peça, cor ou tamanho") }, leadingIcon = { Icon(Icons.Rounded.Search, null) }) }
            if (filtered.isEmpty()) item {
                LiquidHero("TUDO NO SEU TEMPO", if (query.isBlank()) "Sua próxima criação começa aqui." else "Nenhuma peça encontrada.",
                    if (query.isBlank()) "Toque em Nova peça para organizar seu primeiro trabalho." else "Tente outro nome, tamanho ou cor.")
            }

            items(filtered, key = { it.id }) { piece ->
                Card(Modifier.fillMaxWidth().animateContentSize(), colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BubbleIcon(Icons.Rounded.Checkroom)
                            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                                Text(piece.name, style = MaterialTheme.typography.titleMedium)
                                Text("${piece.color.ifBlank { "Sem cor" }} • ${piece.size.ifBlank { "Sem tamanho" }}", color = RoseMuted)
                            }
                            IconButton(onClick = { navController.navigate("edit_piece/${piece.id}") }) { Icon(Icons.Rounded.Edit, "Editar ${piece.name}") }
                        }
                        Text("Quantidade", color = RoseMuted)
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            IconButton(enabled = piece.quantity > 1, onClick = { scope.launch { runCatching { dao.adjustQuantity(piece.id, -1) }.onFailure { snackbar.showSnackbar("Não foi possível atualizar.") } } }) { Icon(Icons.Rounded.Remove, "Diminuir quantidade") }
                            Text(piece.quantity.toString(), style = MaterialTheme.typography.titleMedium)
                            IconButton(enabled = piece.quantity < 99999, onClick = { scope.launch { runCatching { dao.adjustQuantity(piece.id, 1) }.onFailure { snackbar.showSnackbar("Não foi possível atualizar.") } } }) { Icon(Icons.Rounded.Add, "Aumentar quantidade") }
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = { deleting = piece }) { Icon(Icons.Rounded.DeleteOutline, "Excluir ${piece.name}") }
                        }
                    }
                }
            }
        }
    }
    deleting?.let { piece ->
        AlertDialog(onDismissRequest = { deleting = null }, title = { Text("Excluir esta peça?") },
            text = { Text("O registro de ${piece.name} será removido.") },
            confirmButton = { TextButton(onClick = { deleting = null; scope.launch {
                runCatching { dao.delete(piece) }.onFailure { snackbar.showSnackbar("Não foi possível excluir.") }
            } }) { Text("Excluir") } }, dismissButton = { TextButton(onClick = { deleting = null }) { Text("Cancelar") } })
    }
}

