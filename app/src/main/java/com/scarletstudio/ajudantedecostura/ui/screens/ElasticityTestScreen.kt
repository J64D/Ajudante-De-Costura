package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElasticityTestScreen(navController: NavHostController) {
    var stretchedLength by remember { mutableStateOf("14") }
    val length = stretchedLength.replace(',', '.').toDoubleOrNull() ?: 10.0
    val elasticity = (((length - 10.0) / 10.0) * 100).coerceAtLeast(0.0).toInt()
    val result = when {
        elasticity < 10 -> Triple("Baixa", "Tecido firme, com pouca elasticidade.", Color(0xFF4D8B5B))
        elasticity < 30 -> Triple("Média", "Esse tecido possui elasticidade moderada.", Color(0xFFD18A00))
        else -> Triple("Alta", "Esse tecido acompanha bem os movimentos do corpo.", Color(0xFFC04B6E))
    }

    Scaffold(
        bottomBar = { AdMobBanner(modifier = Modifier.fillMaxWidth().height(50.dp)) },
        topBar = {
            TopAppBar(
                title = { Text("Teste de Elasticidade", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        },
        containerColor = Color(0xFFFFF2F6)
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Use uma amostra de 10 cm de tecido.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Estique sem forçar até o limite confortável e informe quanto ela alcançou.", color = Color.Gray)
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Comprimento inicial", fontWeight = FontWeight.Bold)
                    Text("10 cm", color = Color.Gray)
                    Text("Quanto alcançou ao esticar?", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = stretchedLength,
                        onValueChange = { value -> if (value.all { it.isDigit() || it == ',' || it == '.' }) stretchedLength = value },
                        modifier = Modifier.fillMaxWidth(),
                        suffix = { Text("cm") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            }
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Resultado", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("$elasticity%", color = Color(0xFFFF4F9A), fontSize = 42.sp, fontWeight = FontWeight.Black)
                        Text("${result.first} elasticidade", color = result.third, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(result.second, color = Color.DarkGray)
                    }
                    VerticalRuler(elasticity)
                }
            }
            Text("Pode ser indicado para", fontWeight = FontWeight.Bold)
            Text("Blusas ajustadas, vestidos e algumas peças esportivas.", color = Color.DarkGray)
            Text("Costura", fontWeight = FontWeight.Bold)
            Text("Prefira ponto com elasticidade.", color = Color.DarkGray)
            Text("Agulha", fontWeight = FontWeight.Bold)
            Text("Verifique se o tecido exige ponta bola ou stretch.", color = Color.DarkGray)
            Button(
                onClick = { stretchedLength = "10" },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4F9A))
            ) { Text("? Como fazer o teste") }
        }
    }
}

@Composable
private fun VerticalRuler(elasticity: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("^", color = Color(0xFFFF4F9A), fontSize = 22.sp)
        Spacer(Modifier.height((elasticity.coerceIn(0, 80) + 30).dp).background(Color(0xFFFF4F9A)).width(4.dp))
        Text("10 cm", color = Color.Gray, fontSize = 11.sp)
    }
}
