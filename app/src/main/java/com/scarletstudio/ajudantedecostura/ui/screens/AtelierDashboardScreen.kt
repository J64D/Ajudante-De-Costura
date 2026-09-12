@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.R
import com.scarletstudio.ajudantedecostura.data.local.PieceDatabase
import com.scarletstudio.ajudantedecostura.navigation.Routes

private val Pink = Color(0xFFFF4F9A)
private val PalePink = Color(0xFFFFF2F6)

@Composable
fun AtelierDashboardScreen(navController: NavHostController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val pieces by remember { PieceDatabase.getDatabase(context).pieceDao().getAll() }
        .collectAsState(initial = emptyList())
    val totalQuantity = pieces.sumOf { it.quantity }

    Scaffold(
        containerColor = PalePink,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Ajudante de Costura", fontWeight = FontWeight.Bold)
                        Text("Seu ateliê hoje", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Configurações")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PalePink)
            )
        },
        bottomBar = {
            Column {
                AdMobBanner(modifier = Modifier.fillMaxWidth().height(50.dp))
                NavigationBar(containerColor = Color.White) {
                DashboardNavItem("Início", Icons.Default.ShoppingBag, true) { }
                DashboardNavItem("Produção", Icons.Default.CalendarMonth, false) {
                    navController.navigate(Routes.PRODUCTION)
                }
                DashboardNavItem("Ferramentas", Icons.Default.Science, false) {
                    navController.navigate(Routes.FABRICS)
                }
                DashboardNavItem("Aprender", Icons.Default.MenuBook, false) {
                    navController.navigate(Routes.LEARN)
                }
                DashboardNavItem("Mais", Icons.Default.Settings, false) {
                    navController.navigate(Routes.SETTINGS)
                }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(18.dp)) {
                        Text("Resumo do ateliê", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(Modifier.height(14.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            SummaryMetric(totalQuantity.toString(), "em produção")
                            SummaryMetric("0", "concluídas hoje")
                            SummaryMetric("0", "entregas próximas")
                        }
                    }
                }
            }
            item {
                Text("Continuar trabalhando", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            if (pieces.isEmpty()) {
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(18.dp)) {
                            Text("Nenhuma peça cadastrada", fontWeight = FontWeight.Bold)
                            Text("Comece adicionando a encomenda que está na sua mesa.", color = Color.Gray)
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = { navController.navigate(Routes.ADD_PIECE) },
                                colors = ButtonDefaults.buttonColors(containerColor = Pink)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Text(" Nova peça")
                            }
                        }
                    }
                }
            } else {
                items(pieces.take(2), key = { it.id }) { piece ->
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Pink, modifier = Modifier.size(32.dp))
                            Column(Modifier.padding(start = 12.dp)) {
                                Text(piece.name, fontWeight = FontWeight.Bold)
                                Text("${piece.color} • ${piece.size} • ${piece.quantity} unidade(s)", color = Color.Gray)
                                Text("Em produção", color = Pink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            item { Text("Acesso rápido", fontWeight = FontWeight.Bold, fontSize = 18.sp) }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickAction("Precificar", Icons.Default.Paid, Modifier.weight(1f)) { navController.navigate(Routes.PRICING) }
                    QuickAction("Elasticidade", Icons.Default.Science, Modifier.weight(1f)) { navController.navigate(Routes.ELASTICITY) }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickAction("Guia de tecidos", Icons.Default.MenuBook, Modifier.weight(1f)) { navController.navigate(Routes.FABRICS) }
                    QuickAction("Aprender", Icons.Default.MenuBook, Modifier.weight(1f)) { navController.navigate(Routes.LEARN) }
                }
            }
        }
    }
}

@Composable
private fun SummaryMetric(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Pink, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.Gray, fontSize = 11.sp)
    }
}

@Composable
private fun RowScope.DashboardNavItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, onClick: () -> Unit) {
    NavigationBarItem(selected = selected, onClick = onClick, icon = { Icon(icon, contentDescription = label) }, label = { Text(label, fontSize = 10.sp) })
}

@Composable
private fun QuickAction(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = Pink)
            Spacer(Modifier.height(6.dp))
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
