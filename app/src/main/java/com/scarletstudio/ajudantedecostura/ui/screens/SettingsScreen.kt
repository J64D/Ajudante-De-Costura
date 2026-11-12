package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.data.local.PieceDatabase
import com.scarletstudio.ajudantedecostura.data.local.PieceBackup
import com.scarletstudio.ajudantedecostura.data.local.entity.PieceEntity
import com.scarletstudio.ajudantedecostura.navigation.Routes
import com.scarletstudio.ajudantedecostura.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dao = remember { PieceDatabase.getDatabase(context).pieceDao() }
    val scope = rememberCoroutineScope()
    var busy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var clear by remember { mutableStateOf(false) }
    var pending by remember { mutableStateOf<List<PieceEntity>?>(null) }
    val export = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) scope.launch {
            busy = true
            runCatching { withContext(Dispatchers.IO) {
                val content = PieceBackup.encode(dao.getAll().first())
                val stream = context.contentResolver.openOutputStream(uri, "wt") ?: error("Arquivo indisponível")
                stream.bufferedWriter(Charsets.UTF_8).use { it.write(content) }
            } }.onSuccess { message = "Backup salvo com sucesso." }.onFailure { message = "Não foi possível salvar o backup." }
            busy = false
        }
    }
    val import = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) scope.launch {
            busy = true
            runCatching { withContext(Dispatchers.IO) {
                val stream = context.contentResolver.openInputStream(uri) ?: error("Arquivo indisponível")
                val bytes = stream.use { input ->
                    val output = java.io.ByteArrayOutputStream()
                    val buffer = ByteArray(8192)
                    var count = input.read(buffer)
                    while (count != -1) {
                        require(output.size() + count <= 5 * 1024 * 1024)
                        output.write(buffer, 0, count)
                        count = input.read(buffer)
                    }
                    output.toByteArray()
                }
                PieceBackup.decode(bytes.toString(Charsets.UTF_8))
            } }.onSuccess { if (it.isEmpty()) message = "O backup está vazio." else pending = it }
                .onFailure { message = "Backup inválido. Escolha um JSON exportado pelo aplicativo, de até 5 MB." }
            busy = false
        }
    }
    Scaffold(topBar = { AtelierTopBar("Configurações", { navController.popBackStack() }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            InlineAd()
            LiquidHero("SEU ESPAÇO", "Tudo bem\nguardado.", "Cuide dos registros do seu ateliê.")
            if (busy) LinearProgressIndicator(Modifier.fillMaxWidth())
            message?.let { Text(it, color = Rose) }
            Text("Backup das peças", style = MaterialTheme.typography.titleLarge)
            Text("Salve uma cópia em um arquivo e leve seus registros para outro aparelho.", color = RoseMuted)
            Button(enabled = !busy, onClick = { export.launch("ajudante-costura-backup.json") }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                Icon(Icons.Rounded.FileDownload, null); Spacer(Modifier.width(8.dp)); Text("Exportar backup")
            }
            OutlinedButton(enabled = !busy, onClick = { import.launch(arrayOf("application/json", "text/plain", "application/octet-stream")) }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                Icon(Icons.Rounded.FileUpload, null); Spacer(Modifier.width(8.dp)); Text("Importar backup")
            }
            Text("Importar adiciona os registros às peças atuais. Importar o mesmo arquivo novamente cria cópias.", style = MaterialTheme.typography.bodySmall, color = RoseMuted)
            HorizontalDivider()
            AtelierAction("Cursos e publicação", "Acesso às aulas e à administração", Icons.Rounded.OndemandVideo, Modifier.fillMaxWidth()) { navController.navigate(Routes.COURSES) }
            TextButton(onClick = { navController.navigate(Routes.PRIVACY) }) { Icon(Icons.Rounded.PrivacyTip, null); Spacer(Modifier.width(8.dp)); Text("Privacidade e seus dados") }
            OutlinedButton(enabled = !busy, onClick = { clear = true }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text("Excluir todas as peças") }
        }
    }
    if (clear) AlertDialog(onDismissRequest = { clear = false }, title = { Text("Excluir todas as peças?") },
        text = { Text("Esta ação não pode ser desfeita. Exporte um backup antes se quiser guardar seus registros.") },
        confirmButton = { TextButton(onClick = { clear = false; scope.launch {
            busy = true
            runCatching { dao.deleteAll() }.onSuccess { message = "Peças excluídas." }.onFailure { message = "Não foi possível excluir as peças." }
            busy = false
        } }) { Text("Excluir tudo") } }, dismissButton = { TextButton(onClick = { clear = false }) { Text("Cancelar") } })
    pending?.let { rows ->
        AlertDialog(onDismissRequest = { pending = null }, title = { Text("Importar ${rows.size} registros?") },
            text = { Text("As peças existentes serão mantidas. Os registros do arquivo serão adicionados.") },
            confirmButton = { TextButton(onClick = { pending = null; scope.launch {
                busy = true
                runCatching { dao.insertAll(rows) }.onSuccess { message = "Backup importado." }.onFailure { message = "Não foi possível importar o backup." }
                busy = false
            } }) { Text("Importar") } }, dismissButton = { TextButton(onClick = { pending = null }) { Text("Cancelar") } })
    }
}

