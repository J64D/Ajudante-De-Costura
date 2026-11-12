package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Straighten
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.data.SewingMath
import com.scarletstudio.ajudantedecostura.ui.theme.*

@Composable
fun ElasticityTestScreen(navController: NavHostController) {
    var length by rememberSaveable { mutableStateOf("") }
    var help by remember { mutableStateOf(false) }
    val result = SewingMath.elasticity(length)
    val progress by animateFloatAsState(((result ?: 0.0) / 100).toFloat().coerceIn(0f, 1f), spring(), label = "Elasticidade")
    Scaffold(topBar = { AtelierTopBar("Elasticidade", { navController.popBackStack() }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            InlineAd()
            BubbleIcon(Icons.Rounded.Straighten)
            Text("Conheça o movimento\ndo seu tecido.", style = MaterialTheme.typography.headlineSmall)
            Text("Separe uma amostra de 10 cm e meça quanto ela alcança ao esticar suavemente.", color = RoseMuted)
            OutlinedTextField(length, { length = it.take(8) }, label = { Text("Comprimento esticado") }, suffix = { Text("cm") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth(),
                isError = length.isNotBlank() && result == null, supportingText = { Text("Informe de 10 a 100 cm. A medida inicial é 10 cm.") })
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Petal)) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(result?.let { "${java.text.DecimalFormat("0.#").format(it)}%" } ?: "Vamos medir?", style = MaterialTheme.typography.headlineLarge)
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
                    Text(when { result == null -> "O resultado aparecerá aqui."
                        result < 10 -> "Pouca elasticidade"
                        result < 30 -> "Elasticidade moderada"
                        else -> "Alta elasticidade" }, style = MaterialTheme.typography.titleMedium)
                    Text("A barra representa até 100%. O valor acima mostra o cálculo completo.", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text("Confira o molde e teste nos dois sentidos do tecido. A elasticidade, sozinha, não determina a agulha nem a peça ideal.", color = RoseMuted)
            Button(onClick = { help = true }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text("Como fazer o teste") }
            TextButton(onClick = { navController.navigate(com.scarletstudio.ajudantedecostura.navigation.Routes.RULER) }) { Text("Abrir régua com rolagem") }
            TextButton(onClick = { length = "" }) { Text("Limpar medida") }
        }
    }
    if (help) AlertDialog(onDismissRequest = { help = false }, title = { Text("Passo a passo") },
        text = { Text("1. Marque 10 cm no tecido em repouso.\n\n2. Segure nas marcas e estique sem deformar o tecido.\n\n3. Meça a distância entre as marcas e informe o valor.\n\n4. Solte e observe se ele volta ao tamanho inicial. Repita no outro sentido.") },
        confirmButton = { TextButton(onClick = { help = false }) { Text("Entendi") } })
}

