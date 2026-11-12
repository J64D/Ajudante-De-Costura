package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.navigation.Routes
import com.scarletstudio.ajudantedecostura.ui.theme.*

@Composable
fun LearnScreen(navController: NavHostController) {
    Scaffold(topBar = { AtelierTopBar("Aprender", { navController.popBackStack() }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { InlineAd() }
            item { Text("Aprender costura", style = MaterialTheme.typography.headlineMedium) }
            item { AtelierAction("Cursos e módulos", "Aulas com player do YouTube dentro do app", Icons.Rounded.OndemandVideo, Modifier.fillMaxWidth()) { navController.navigate(Routes.COURSES) } }
            item { AtelierAction("Guia de tecidos", "Características, usos e cuidados", Icons.Rounded.Layers, Modifier.fillMaxWidth()) { navController.navigate(Routes.FABRICS) } }
            item { Text("Dicas para o dia a dia", style = MaterialTheme.typography.titleLarge) }
            item { SewingTip("Antes de cortar", "Confira as medidas, o sentido do fio e as margens indicadas no molde. Posicione todas as partes antes do primeiro corte.") }
            item { SewingTip("Teste antes de costurar", "Use um retalho do mesmo tecido para ajustar o ponto e a tensão. Confira o resultado dos dois lados antes de seguir para a peça.") }
            item { SewingTip("Organize cada trabalho", "Registre nome, tamanho, cor e quantidade. Separe os aviamentos junto ao tecido para facilitar a próxima etapa.") }
            item { AtelierAction("Moldes", "Projetos básicos e arquivos PLT", Icons.Rounded.ContentCut, Modifier.fillMaxWidth()) { navController.navigate(Routes.PATTERNS) } }

        }
    }
}
@Composable
private fun SewingTip(title: String, body: String) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Card(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth().animateContentSize()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row { Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium); Icon(if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore, if (expanded) "Recolher dica" else "Ler dica") }
            if (expanded) Text(body, color = RoseMuted)
        }
    }
}

