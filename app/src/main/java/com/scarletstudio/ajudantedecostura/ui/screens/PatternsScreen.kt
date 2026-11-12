package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.data.PatternPlot
import com.scarletstudio.ajudantedecostura.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

private data class PatternEntry(val id: String, val title: String, val note: String, val source: String? = null, val rectangles: List<Pair<Int, Int>> = emptyList())
private val patterns = listOf(
    PatternEntry("ARD-045", "ARD-045", "Molde vetorial reconstruído a partir da referência enviada, com contornos e marcações. Tamanhos e margens ainda precisam ser conferidos antes do corte.", "native"),
    PatternEntry("almofada", "Capa de almofada envelope", "Para enchimento de 40 × 40 cm. Cortar: frente 42 × 42 cm e duas costas 42 × 30 cm (o retângulo menor representa cada costa). Margem de 1 cm incluída. Faça bainha dupla de 1 cm numa borda de 42 cm de cada costa. Sobreponha as costas, direito com direito com a frente, costure o contorno a 1 cm e desvire.", rectangles = listOf(42 to 42, 42 to 30)),
    PatternEntry("sacola", "Sacola simples", "Cortar duas partes de 37 × 42 cm e duas alças de 10 × 60 cm. Os retângulos representam uma parte de cada tipo. Margens incluídas. Una laterais e fundo a 1 cm; faça bainha dupla de 2 cm no topo. Dobre as bordas longas das alças até o centro e dobre ao meio; pesponte. Fixe as alças com costura reforçada. Teste a resistência antes de usar.", rectangles = listOf(37 to 42, 10 to 60)),
    PatternEntry("saquinho", "Saquinho com cordão", "Cortar duas partes de 22 × 28 cm; o desenho representa uma delas. Margens incluídas. Una laterais e fundo a 1 cm, deixando 4 cm livres no topo das laterais. Faça bainha de 1 cm e dobre mais 2 cm para o canal. Passe o cordão pelo canal. Tamanho aproximado pronto: 20 × 24 cm.", rectangles = listOf(22 to 28))
)
private fun PatternEntry.content(): String = buildString {
    append("IN;SP1;")
    var x = 0
    rectangles.forEach { (w, h) ->
        val right = x + w * 400; val top = h * 400
        append("PU$x,0;PD$right,0,$right,$top,$x,$top,$x,0;")
        x = right + 1200
    }
    append("SP0;")
}

@Composable
fun PatternsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val directory = remember { File(context.filesDir, "patterns").apply { mkdirs() } }
    var imported by remember { mutableStateOf(emptyList<PatternEntry>()) }
    var selected by remember { mutableStateOf<PatternEntry?>(null) }
    var plot by remember { mutableStateOf<PatternPlot?>(null) }
    var original by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var exportContent by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    suspend fun refresh() { imported = withContext(Dispatchers.IO) {
        directory.listFiles()?.filter { it.extension == "plt" }?.sortedBy { it.name }?.map {
            PatternEntry(it.nameWithoutExtension, "Molde importado ${it.nameWithoutExtension.take(8)}", "Arquivo importado. Confira as medidas e as margens antes de cortar.", it.absolutePath)
        }.orEmpty()
    } }
    LaunchedEffect(Unit) { refresh() }
    val save = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { uri ->
        if (uri != null) scope.launch {
            busy = true
            runCatching { withContext(Dispatchers.IO) {
                (context.contentResolver.openOutputStream(uri, "wt") ?: error("Arquivo indisponível")).bufferedWriter().use { it.write(exportContent) }
            } }.onSuccess { message = "Arquivo salvo." }.onFailure { message = "Não foi possível salvar o arquivo." }
            busy = false
        }
    }
    val import = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) scope.launch {
            busy = true; message = null
            runCatching { withContext(Dispatchers.IO) {
                val bytes = (context.contentResolver.openInputStream(uri) ?: error("Arquivo indisponível")).use { stream ->
                    val out = java.io.ByteArrayOutputStream(); val buffer = ByteArray(8192)
                    var n = stream.read(buffer)
                    while (n != -1) { require(out.size() + n <= 5 * 1024 * 1024) { "Máximo de 5 MB." }; out.write(buffer, 0, n); n = stream.read(buffer) }
                    out.toByteArray()
                }
                val text = bytes.toString(Charsets.UTF_8)
                PatternPlot.parse(text)
                File(directory, UUID.randomUUID().toString() + ".plt").writeText(text)
            } }.onSuccess { refresh(); message = "Molde importado e salvo no aplicativo." }
                .onFailure { message = it.message ?: "PLT inválido." }
            busy = false
        }
    }
    LaunchedEffect(selected) {
        plot = null; original = ""; message = null
        val entry = selected ?: return@LaunchedEffect
        busy = true
        runCatching { withContext(Dispatchers.IO) {
            if (entry.source == "native") {
                val data = org.json.JSONObject(context.assets.open("patterns/ard045.json").bufferedReader().use { it.readText() })
                val segments = data.getJSONArray("segments")
                val drawing = PatternPlot((0 until segments.length()).map { index ->
                    val row = segments.getJSONArray(index)
                    com.scarletstudio.ajudantedecostura.data.PlotLine(
                        com.scarletstudio.ajudantedecostura.data.PlotPoint(row.getDouble(0).toFloat(), row.getDouble(1).toFloat()),
                        com.scarletstudio.ajudantedecostura.data.PlotPoint(row.getDouble(2).toFloat(), row.getDouble(3).toFloat()))
                })
                return@withContext drawing.plt() to drawing
            }
            val content = when (entry.source) {
                null -> entry.content()
                else -> File(entry.source).readText()
            }
            content to PatternPlot.parse(content)
        } }.onSuccess { original = it.first; plot = it.second }.onFailure { message = "Não foi possível abrir: ${it.message}" }
        busy = false
    }
    BackHandler(selected != null) { selected = null }
    Scaffold(topBar = { AtelierTopBar(selected?.title ?: "Moldes", { if (selected != null) selected = null else navController.popBackStack() }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { InlineAd() }
            if (busy) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
            message?.let { item { Text(it, color = Rose) } }
            val entry = selected
            if (entry == null) {
                item { Text("Biblioteca de moldes", style = MaterialTheme.typography.headlineSmall) }
                item { Text("Abra o desenho, confira as instruções e salve uma cópia para imprimir ou usar no seu programa de modelagem.", color = RoseMuted) }
                item { OutlinedButton(enabled = !busy, onClick = { import.launch(arrayOf("*/*")) }) { Text("Importar arquivo PLT") } }
                items(patterns + imported, key = { it.id }) { item ->
                    Card(onClick = { selected = item }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(item.title, style = MaterialTheme.typography.titleMedium)
                            Text(if (item.source == null) "Projeto básico • medidas e instruções" else if (item.source == "native") "Molde vetorial • reconstruído da referência" else "PLT importado", color = RoseMuted)
                        }
                    }
                }

            } else {
                item { Text(entry.note, style = MaterialTheme.typography.bodyLarge) }
                plot?.let { drawing ->
                    item { PlotPreview(drawing) }
                    item { Text("Use dois dedos para ampliar e mover. A prévia é reduzida para caber na tela.", color = RoseMuted) }
                    item { Text("Área do desenho: ${java.text.DecimalFormat("0.0").format(drawing.width / 400)} × ${java.text.DecimalFormat("0.0").format(drawing.height / 400)} cm, considerando a unidade padrão PLT.") }
                    item { Text("O SVG mantém dimensões físicas. Imprima a 100%, sem ajustar à página, usando mosaico ou plotter se necessário. Confira uma medida conhecida antes do corte.", style = MaterialTheme.typography.bodySmall) }
                    item { Button(enabled = !busy, onClick = { exportContent = drawing.svg(); save.launch(entry.id + ".svg") }, modifier = Modifier.fillMaxWidth()) { Text("Salvar molde em SVG") } }
                    item { OutlinedButton(enabled = !busy, onClick = { exportContent = original; save.launch(entry.id + ".plt") }, modifier = Modifier.fillMaxWidth()) { Text("Exportar para plotter (PLT)") } }
                    item { TextButton(onClick = { exportContent = entry.title + "\n\n" + entry.note; save.launch(entry.id + "-instrucoes.txt") }) { Text("Salvar instruções") } }
                }
            }
        }
    }
}

@Composable
private fun PlotPreview(plot: PatternPlot) {
    var zoom by remember(plot) { mutableFloatStateOf(1f) }
    var pan by remember(plot) { mutableStateOf(Offset.Zero) }
    Column {
        Surface(color = androidx.compose.ui.graphics.Color.White, shape = MaterialTheme.shapes.small) {
            Canvas(Modifier.fillMaxWidth().height(350.dp).pointerInput(plot) {
                detectTransformGestures { _, shift, scale, _ -> zoom = (zoom * scale).coerceIn(1f, 12f); pan += shift }
            }) {
                val fit = minOf((size.width - 32.dp.toPx()) / plot.width, (size.height - 32.dp.toPx()) / plot.height)
                val origin = Offset((size.width - plot.width * fit * zoom) / 2, (size.height - plot.height * fit * zoom) / 2) + pan
                fun point(p: com.scarletstudio.ajudantedecostura.data.PlotPoint) = origin + Offset((p.x - plot.minX) * fit * zoom, (plot.height - p.y + plot.minY) * fit * zoom)
                plot.lines.forEach { drawLine(RoseInk, point(it.from), point(it.to), 1.dp.toPx()) }
            }
        }
        TextButton(onClick = { zoom = 1f; pan = Offset.Zero }) { Text("Ajustar desenho à tela") }
    }
}

