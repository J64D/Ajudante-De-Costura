package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.navigation.Routes

private data class Fabric(val name: String, val stretch: String, val use: String, val tip: String)
private val fabrics = listOf(
    Fabric("Malha", "média a alta", "blusas e vestidos", "Use agulha ponta bola e ponto com elasticidade."),
    Fabric("Tricoline", "baixa", "camisas e peças infantis", "Estável e fácil de cortar."),
    Fabric("Jeans", "baixa", "calças e jaquetas", "Use agulha para jeans e linha resistente."),
    Fabric("Linho", "baixa", "vestidos e camisas", "Pode encolher; lave antes do corte."),
    Fabric("Cetim", "baixa", "festa e lingerie", "Prenda bem antes de cortar para evitar escorregões."),
    Fabric("Viscose", "baixa", "vestidos e blusas", "Pode escorregar e alguns tipos encolhem."),
    Fabric("Tule", "baixa", "saias e detalhes", "Corte em camada única sobre base estável."),
    Fabric("Moletom", "média", "casacos e conjuntos", "Prefira agulha stretch e pontos flexíveis."),
    Fabric("Suplex", "alta", "peças esportivas", "Teste a elasticidade nos dois sentidos."),
    Fabric("Organza", "baixa", "detalhes e festa", "Use tesoura bem afiada e pouca manipulação.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FabricGuideScreen(navController: NavHostController) {
    var query by remember { mutableStateOf("") }
    val filtered = fabrics.filter { it.name.contains(query, ignoreCase = true) }
    Scaffold(
        bottomBar = { AdMobBanner(modifier = Modifier.fillMaxWidth().height(50.dp)) },
        topBar = {
            TopAppBar(
                title = { Text("Guia de Tecidos", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    placeholder = { Text("Buscar tecido") }
                )
            }
            items(filtered) { fabric ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF2F6))) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(fabric.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Elasticidade: ${fabric.stretch}")
                        Text("Usos comuns: ${fabric.use}", color = Color.DarkGray)
                        Text("Dica: ${fabric.tip}", color = Color.Gray)
                        androidx.compose.material3.TextButton(onClick = { navController.navigate(Routes.ELASTICITY) }) {
                            Text("Testar elasticidade deste tecido", color = Color(0xFFFF4F9A))
                        }
                    }
                }
            }
        }
    }
}
