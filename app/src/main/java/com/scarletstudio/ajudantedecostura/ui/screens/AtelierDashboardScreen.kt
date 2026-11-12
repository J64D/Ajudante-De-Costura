package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.data.local.PieceDatabase
import com.scarletstudio.ajudantedecostura.navigation.Routes
import com.scarletstudio.ajudantedecostura.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtelierDashboardScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dao = remember { PieceDatabase.getDatabase(context).pieceDao() }
    val pieces by dao.getAll().collectAsState(initial = emptyList())
    Scaffold(
        topBar = { TopAppBar(title = { Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            BrandLogo(Modifier.size(40.dp))
            Spacer(Modifier.width(10.dp))
            Text("Meu ateliê", style = MaterialTheme.typography.titleLarge)
        } },
            actions = { IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) { Icon(Icons.Rounded.Settings, "Configurações") } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Blush)) },
        bottomBar = {
            Column {

            Surface(Modifier.padding(horizontal = 20.dp, vertical = 8.dp).navigationBarsPadding(), shape = RoundedCornerShape(32.dp), shadowElevation = 4.dp) {
                NavigationBar(containerColor = androidx.compose.ui.graphics.Color.White, windowInsets = WindowInsets(0)) {
                    listOf(Triple("Início", Icons.Rounded.Home, Routes.HOME),
                        Triple("Peças", Icons.Rounded.Checkroom, Routes.PRODUCTION),
                        Triple("Aprender", Icons.Rounded.OndemandVideo, Routes.LEARN)).forEach { (label, icon, route) ->
                        NavigationBarItem(selected = route == Routes.HOME, onClick = { if (route != Routes.HOME) navController.navigate(route) },
                            icon = { Icon(icon, label) }, label = { Text(label) })
                    }
                }
            }
            }
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            item { InlineAd() }
            item {
                Text("Seu ateliê", style = MaterialTheme.typography.headlineMedium)
                Text("Consulte as peças ou comece um novo trabalho.", color = RoseMuted)
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)) {
                    Row(Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column { Text(pieces.sumOf { it.quantity.toLong() }.toString(), style = MaterialTheme.typography.headlineMedium); Text("peças", color = RoseMuted) }
                        Column { Text(pieces.size.toString(), style = MaterialTheme.typography.headlineMedium); Text("registros", color = RoseMuted) }
                        Column { Text(pieces.map { it.color.lowercase() }.filter { it.isNotBlank() }.distinct().size.toString(), style = MaterialTheme.typography.headlineMedium); Text("cores", color = RoseMuted) }
                    }
                }
            }
            item { Button(onClick = { navController.navigate(Routes.ADD_PIECE) }, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)) {
                Icon(Icons.Rounded.Add, null); Spacer(Modifier.width(8.dp)); Text("Adicionar uma peça")
            } }
            item { Text("Ferramentas", style = MaterialTheme.typography.titleLarge) }
            item { Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AtelierAction("Régua", "Medir e consultar centímetros", Icons.Rounded.Straighten, Modifier.weight(1f)) { navController.navigate(Routes.RULER) }
                AtelierAction("Moldes", "Abrir e salvar arquivos", Icons.Rounded.ContentCut, Modifier.weight(1f)) { navController.navigate(Routes.PATTERNS) }
            } }
            item { Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AtelierAction("Minhas peças", "Consultar e organizar", Icons.Rounded.Checkroom, Modifier.weight(1f)) { navController.navigate(Routes.PRODUCTION) }
                AtelierAction("Preço justo", "Material + seu trabalho", Icons.Rounded.Calculate, Modifier.weight(1f)) { navController.navigate(Routes.PRICING) }
            } }
            item { Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AtelierAction("Elasticidade", "Conheça seu tecido", Icons.Rounded.Straighten, Modifier.weight(1f)) { navController.navigate(Routes.ELASTICITY) }
                AtelierAction("Cursos", "Módulos e videoaulas", Icons.Rounded.OndemandVideo, Modifier.weight(1f)) { navController.navigate(Routes.COURSES) }
            } }
            item { AtelierAction("Guia de tecidos", "Consulte características e cuidados", Icons.Rounded.Layers, Modifier.fillMaxWidth()) { navController.navigate(Routes.FABRICS) } }

        }
    }
}

