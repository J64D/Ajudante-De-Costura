package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.scarletstudio.ajudantedecostura.data.SewingMath
import com.scarletstudio.ajudantedecostura.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PriceCalculatorScreen(navController: NavHostController) {
    var material by rememberSaveable { mutableStateOf("") }
    var hours by rememberSaveable { mutableStateOf("") }
    var rate by rememberSaveable { mutableStateOf("") }
    var markup by rememberSaveable { mutableStateOf("30") }
    val price = SewingMath.price(material, hours, rate, markup)
    val money = remember { NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")) }
    Scaffold(topBar = { AtelierTopBar("Preço justo", { navController.popBackStack() }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            InlineAd()
            BubbleIcon(Icons.Rounded.Calculate)
            Text("Seu trabalho tem valor.", style = MaterialTheme.typography.headlineSmall)
            Text("Considere os materiais, seu tempo e o acréscimo que deseja aplicar.", color = RoseMuted)
            PriceInput("Materiais (R$)", material, { material = it }, "Tecidos, linhas, botões e outros custos.", 1000000.0)
            PriceInput("Tempo de trabalho (horas)", hours, { hours = it }, "Ex.: 2,5 para duas horas e meia.", 100000.0)
            PriceInput("Valor da sua hora (R$)", rate, { rate = it }, "Quanto você deseja receber por hora.", 1000000.0)
            PriceInput("Acréscimo sobre o custo (%)", markup, { markup = it }, "30% de acréscimo não é uma margem de 30% sobre a venda.", 1000.0)
            Card(Modifier.fillMaxWidth().animateContentSize(), colors = CardDefaults.cardColors(containerColor = Petal)) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Preço sugerido", style = MaterialTheme.typography.titleMedium)
                    Text(price?.let { money.format(it) } ?: "Preencha os valores", style = MaterialTheme.typography.headlineMedium)
                    Text(if (price != null) "Materiais + (horas × valor da hora), com $markup% de acréscimo." else "Use números válidos em todos os campos.", color = RoseMuted)
                }
            }
            OutlinedButton(onClick = { material = ""; hours = ""; rate = ""; markup = "30" }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text("Novo cálculo") }
        }
    }
}
@Composable
private fun PriceInput(label: String, value: String, onChange: (String) -> Unit, hint: String, max: Double) {
    val parsed = SewingMath.decimal(value)
    val invalid = value.isNotBlank() && (parsed == null || parsed > max)
    OutlinedTextField(value, { onChange(it.take(16)) }, label = { Text(label) }, singleLine = true,
        modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        isError = invalid, supportingText = { Text(if (invalid) "Informe um número de 0 a $max." else hint) })
}

