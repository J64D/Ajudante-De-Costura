package com.scarletstudio.ajudantedecostura.ui.screens

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.scarletstudio.ajudantedecostura.R
import com.scarletstudio.ajudantedecostura.data.local.PieceDatabase
import com.scarletstudio.ajudantedecostura.data.local.dao.PieceDao
import com.scarletstudio.ajudantedecostura.data.local.entity.PieceEntity
import com.scarletstudio.ajudantedecostura.navigation.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val context = LocalContext.current
    val dao = remember { PieceDatabase.getDatabase(context).pieceDao() }
    val pieces by dao.getAll().collectAsState(initial = emptyList())
    val totalPieces = pieces.sumOf { it.quantity }

    val scope = rememberCoroutineScope()
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.9f else 1f, label = "scale")

    Scaffold(
        containerColor = Color.White,

        topBar = {
            Surface(
                color = Color.White,
                modifier = Modifier.statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Minhas Peças",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF4F9A)
                    )

                    IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = "Configurações",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.ADD_PIECE) },
                containerColor = Color(0xFFFF4F9A),
                shape = CircleShape,
                interactionSource = interaction,
                modifier = Modifier
                    .padding(bottom = 80.dp) // Ajuste para não cobrir o bottom bar
                    .size(60.dp)
                    .scale(scale)
                    .shadow(10.dp, CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Adicionar",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        },

        floatingActionButtonPosition = FabPosition.End,

        bottomBar = {
            Column(modifier = Modifier.navigationBarsPadding().background(Color.White)) {
                BottomAppBar(
                    containerColor = Color.White,
                    tonalElevation = 6.dp,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_box),
                                contentDescription = null,
                                tint = Color(0xFFFF4F9A),
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(Modifier.width(8.dp))

                            Column {
                                Text(
                                    "TOTAL ACUMULADO",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFF69B4),
                                    fontWeight = FontWeight.Bold
                                )
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        totalPieces.toString(),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(" peças", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 2.dp))
                                }
                            }
                        }

                        Button(
                            onClick = { navController.navigate(Routes.ORDER_SUMMARY) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFC8DA)
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text("Fechar →", fontSize = 14.sp, color = Color(0xFFFF4F9A), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // ================= CONTEÚDO =================

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 64.dp)
            ) {
                // SEÇÃO FERRAMENTAS PRO (Atrativo Profissional)
                Text(
                    "Minhas Ferramentas",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProToolButton(
                        title = "Calcular\nPreço",
                        icon = R.drawable.ic_empty_clothes, // Usando ícone existente para teste
                        color = Color(0xFFFF4F9A),
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Routes.PRICING) }
                    )
                    ProToolButton(
                        title = "Régua de\nElasticidade",
                        icon = R.drawable.ic_box,
                        color = Color(0xFF9C27B0),
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Routes.ELASTICITY) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (pieces.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_empty_clothes),
                            contentDescription = null,
                            modifier = Modifier.size(110.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(Modifier.height(10.dp))
                        Text("Estoque Vazio", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Toque no + rosa para começar",
                            fontSize = 12.sp,
                            color = Color(0xFFFF4F9A)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentPadding = PaddingValues(bottom = 140.dp, top = 8.dp)
                    ) {
                        items(pieces, key = { it.id }) { piece ->
                            PieceItemUI(piece, dao, scope)
                        }
                    }
                }
            }

            // ================= BANNER FIXO =================
            // Usando Box para garantir que o banner não sobreponha o conteúdo de forma rígida
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                AdMobBanner(
                    modifier = Modifier.fillMaxSize()
                )
            }

            // ================= BOTÃO CENTRAL =================

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 18.dp)
                    .size(60.dp)
                    .shadow(10.dp, CircleShape)
                    .background(Color(0xFFFF4F9A), CircleShape)
                    .clickable { navController.navigate(Routes.MEASURE) },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_empty_clothes),
                    contentDescription = "Medir",
                    modifier = Modifier.size(28.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

/* ================= ADMOB ================= */

@Composable
fun AdMobBanner(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context: Context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-1367515717522293/6625687240"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

/* ================= ITEM ================= */

@Composable
fun PieceItemUI(
    piece: PieceEntity,
    dao: PieceDao,
    scope: CoroutineScope
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(piece.size.uppercase(), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(piece.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(piece.color, fontSize = 13.sp, color = Color.Gray)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(Color.LightGray, CircleShape)
                        .clickable {
                            if (piece.quantity > 1) {
                                scope.launch(Dispatchers.IO) {
                                    dao.update(piece.copy(quantity = piece.quantity - 1))
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) { Text("−") }

                Text(piece.quantity.toString(), fontWeight = FontWeight.Bold, fontSize = 13.sp)

                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(Color(0xFFFF4F9A), CircleShape)
                        .clickable {
                            scope.launch(Dispatchers.IO) {
                                dao.update(piece.copy(quantity = piece.quantity + 1))
                            }
                        },
                    contentAlignment = Alignment.Center
                ) { Text("+", color = Color.White) }

                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(Color(0xFFFFE1E1), CircleShape)
                        .clickable {
                            scope.launch(Dispatchers.IO) {
                                dao.delete(piece)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_trash),
                        contentDescription = "Excluir",
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun ProToolButton(
    title: String,
    icon: Int,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.92f else 1f, label = "btn-scale")

    Card(
        onClick = onClick,
        modifier = modifier
            .height(110.dp)
            .scale(scale),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, color.copy(alpha = 0.3f)),
        interactionSource = interactionSource
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                color = color.copy(alpha = 0.2f),
                shape = CircleShape
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}
