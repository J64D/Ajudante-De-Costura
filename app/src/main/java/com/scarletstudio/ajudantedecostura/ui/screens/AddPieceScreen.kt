package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.data.local.PieceDatabase
import com.scarletstudio.ajudantedecostura.data.local.entity.PieceEntity
import com.scarletstudio.ajudantedecostura.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AddPieceScreen(navController: NavHostController, pieceId: Int? = null) {
    val context = LocalContext.current
    val dao = remember { PieceDatabase.getDatabase(context).pieceDao() }
    val scope = rememberCoroutineScope()
    var name by rememberSaveable { mutableStateOf("") }
    var color by rememberSaveable { mutableStateOf("") }
    var size by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("1") }
    var date by rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    var loaded by rememberSaveable { mutableStateOf(pieceId == null) }
    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(pieceId) {
        if (!loaded && pieceId != null) {
            runCatching { dao.find(pieceId) }.onSuccess { piece ->
                if (piece == null) error = "Esta peça não foi encontrada."
                else { name = piece.name; color = piece.color; size = piece.size; quantity = piece.quantity.toString(); date = piece.date; loaded = true }
            }.onFailure { error = "Não foi possível carregar a peça." }
        }
    }
    Scaffold(topBar = { AtelierTopBar(if (pieceId == null) "Nova peça" else "Editar peça", { if (!busy) navController.popBackStack() }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            BubbleIcon(Icons.Rounded.Checkroom)
            Text("Cada detalhe, no seu lugar.", style = MaterialTheme.typography.headlineSmall)
            Text("Registre sua peça para consultar e ajustar quando precisar.", color = RoseMuted)
            OutlinedTextField(name, { name = it.take(120) }, label = { Text("Nome da peça") }, placeholder = { Text("Ex.: Vestido") }, singleLine = true, enabled = !busy && loaded, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(color, { color = it.take(80) }, label = { Text("Cor") }, placeholder = { Text("Ex.: Rosa") }, singleLine = true, enabled = !busy && loaded, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(size, { size = it.take(40) }, label = { Text("Tamanho") }, placeholder = { Text("Ex.: M ou 42") }, singleLine = true, enabled = !busy && loaded, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(quantity, { quantity = it.filter(Char::isDigit).take(5) }, label = { Text("Quantidade") }, singleLine = true,
                isError = quantity.toIntOrNull() !in 1..99999, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = !busy && loaded, modifier = Modifier.fillMaxWidth(), supportingText = { Text("De 1 a 99.999 peças.") })
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Button(enabled = loaded && !busy && name.isNotBlank() && quantity.toIntOrNull() in 1..99999,
                onClick = {
                    busy = true; error = null
                    scope.launch {
                        val piece = PieceEntity(id = pieceId ?: 0, name = name.trim(), color = color.trim(), size = size.trim(), quantity = quantity.toInt(), date = date)
                        runCatching { if (pieceId == null) dao.insert(piece) else dao.update(piece) }
                            .onSuccess { navController.popBackStack() }
                            .onFailure { error = "Não foi possível salvar. Tente novamente." }
                        busy = false
                    }
                }, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)) { Text(if (busy) "Salvando…" else "Salvar peça") }
        }
    }
}
