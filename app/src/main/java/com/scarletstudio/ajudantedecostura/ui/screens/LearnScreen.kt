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
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(navController: NavHostController) {
    var tab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Vídeos", "Moldes", "Dicas")
    Scaffold(
        bottomBar = { AdMobBanner(modifier = Modifier.fillMaxWidth().height(50.dp)) },
        topBar = {
            TopAppBar(
                title = { Text("Aprender", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = tab) {
                tabs.forEachIndexed { index, title -> Tab(selected = tab == index, onClick = { tab = index }, text = { Text(title) }) }
            }
            val content = when (tab) {
                0 -> listOf("Como tirar medidas corretamente" to "Iniciante • 5 min", "Como colocar zíper invisível" to "Intermediário • 8 min")
                1 -> listOf("Vestido básico" to "P • M • G • GG • Iniciante", "Saia evasê" to "P • M • G • Iniciante")
                else -> listOf("Como escolher a agulha" to "Ferramentas", "Corte sem desperdiçar tecido" to "Planejamento")
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(content) { (title, detail) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { if (tab == 1) navController.navigate(Routes.ADD_PIECE) },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF2F6))
                    ) {
                        androidx.compose.foundation.layout.Row(Modifier.padding(16.dp)) {
                            if (tab == 0) Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color(0xFFFF4F9A))
                            Column(Modifier.padding(start = 12.dp)) {
                                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(detail, color = Color.Gray, fontSize = 13.sp)
                                if (tab == 1) Text("Ver molde  •  Criar peça", color = Color(0xFFFF4F9A), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
