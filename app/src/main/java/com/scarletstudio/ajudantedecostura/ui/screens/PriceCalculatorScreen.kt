package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun PriceCalculatorScreen(navController: NavHostController) {
    var step by remember { mutableIntStateOf(1) }
    
    // Estados do cálculo
    var materialCost by remember { mutableStateOf("") }
    var laborTime by remember { mutableStateOf("") }
    var hourlyRate by remember { mutableStateOf("") }
    var profitMargin by remember { mutableStateOf("30") } // 30% padrão

    val totalPrice: Double by remember(materialCost, laborTime, hourlyRate, profitMargin) {
        derivedStateOf {
            val mat = materialCost.toDoubleOrNull() ?: 0.0
            val time = laborTime.toDoubleOrNull() ?: 0.0
            val rate = hourlyRate.toDoubleOrNull() ?: 0.0
            val profit = (profitMargin.toDoubleOrNull() ?: 0.0) / 100.0
            
            val cost = mat + (time * rate)
            cost * (1 + profit)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assistente de Preço") },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (step > 1) step-- else navController.popBackStack() 
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indicador de progresso simples
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(4) { idx ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .background(
                                if (step > idx) Color(0xFFFF4F9A) else Color(0xFFFFE1E1),
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Conteúdo Animado por Passo
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                    } else {
                        slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                    }
                },
                label = "step-animation"
            ) { targetStep ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    when (targetStep) {
                        1 -> StepContent(
                            question = "Quanto você gastou de material?",
                            description = "Soma de tecidos, linhas, botões, etc.",
                            value = materialCost,
                            onValueChange = { materialCost = it },
                            placeholder = "R$ 0,00"
                        )
                        2 -> StepContent(
                            question = "Quantas horas levou para fazer?",
                            description = "Conte o tempo de corte, costura e acabamento.",
                            value = laborTime,
                            onValueChange = { laborTime = it },
                            placeholder = "Ex: 2.5 horas"
                        )
                        3 -> StepContent(
                            question = "Qual o valor da sua hora?",
                            description = "Quanto você cobra pelo seu trabalho por hora?",
                            value = hourlyRate,
                            onValueChange = { hourlyRate = it },
                            placeholder = "Ex: R$ 20,00"
                        )
                        4 -> ResultContent(totalPrice)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (step < 4) {
                Button(
                    onClick = { step++ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4F9A))
                ) {
                    Text("Continuar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4F9A))
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Concluir", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StepContent(
    question: String,
    description: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Text(question, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    Text(description, fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
    
    Spacer(modifier = Modifier.height(20.dp))
    
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.isEmpty() || it.all { c -> c.isDigit() || c == '.' }) onValueChange(it) },
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth().height(64.dp),
        shape = RoundedCornerShape(16.dp),
        textStyle = LocalTextStyle.current.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
fun ResultContent(totalPrice: Double) {
    Text("PREÇO SURGERIDO", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
    
    Surface(
        color = Color(0xFFFDE7F0),
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Text(
            "R$ %.2f".format(totalPrice),
            fontSize = 42.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFFF4F9A),
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 20.dp)
        )
    }
    
    Text(
        "Este preço já inclui o custo dos materiais,\no seu tempo de trabalho e um lucro de 30%.",
        textAlign = TextAlign.Center,
        fontSize = 14.sp,
        color = Color.DarkGray
    )
}
