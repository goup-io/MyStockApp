package com.example.mystockapp.telas

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.mystockapp.R
import com.example.mystockapp.telas.componentes.Header
import com.example.mystockapp.telas.componentes.MenuDrawer
import com.example.mystockapp.ui.theme.MyStockAppTheme

class BipScreen : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your app.
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStockAppTheme {
                MenuDrawer(titulo = "Busca") {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Screen(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            else -> {
                // You can directly ask for the permission.
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

@Composable
fun Screen(modifier: Modifier = Modifier) {
    var barcodeNumber by remember { mutableStateOf("AB12345678910") }
    var isScanning by remember { mutableStateOf(false) }

    if (isScanning) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF355070)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BarcodeScannerView(
                onScanned = { result ->
                    barcodeNumber = result
                    isScanning = false
                },
                onError = { error ->
                    barcodeNumber = "Erro: $error"
                    isScanning = false
                }
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF355070)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFE7E7E7),
                        shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(5.dp))

                Column(
                    modifier = modifier
                        .fillMaxWidth(0.75f)
                        .height(180.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {

                    // Container que simula o scanner com as bordas arredondadas
                    Box(
                        modifier = Modifier
                            .size(250.dp)
                            .background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        // Canvas para desenhar as bordas arredondadas
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 4.dp.toPx()
                            val cornerLength = 40.dp.toPx()

                            // Desenhando cantos superiores
                            drawLine(
                                color = Color(0xFF355070),
                                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                end = androidx.compose.ui.geometry.Offset(cornerLength, 0f),
                                strokeWidth = strokeWidth
                            )
                            drawLine(
                                color = Color(0xFF355070),
                                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                end = androidx.compose.ui.geometry.Offset(0f, cornerLength),
                                strokeWidth = strokeWidth
                            )
                            drawLine(
                                color = Color(0xFF355070),
                                start = androidx.compose.ui.geometry.Offset(size.width, 0f),
                                end = androidx.compose.ui.geometry.Offset(size.width - cornerLength, 0f),
                                strokeWidth = strokeWidth
                            )
                            drawLine(
                                color = Color(0xFF355070),
                                start = androidx.compose.ui.geometry.Offset(size.width, 0f),
                                end = androidx.compose.ui.geometry.Offset(size.width, cornerLength),
                                strokeWidth = strokeWidth
                            )

                            // Desenhando cantos inferiores
                            drawLine(
                                color = Color(0xFF355070),
                                start = androidx.compose.ui.geometry.Offset(0f, size.height),
                                end = androidx.compose.ui.geometry.Offset(cornerLength, size.height),
                                strokeWidth = strokeWidth
                            )
                            drawLine(
                                color = Color(0xFF355070),
                                start = androidx.compose.ui.geometry.Offset(0f, size.height),
                                end = androidx.compose.ui.geometry.Offset(0f, size.height - cornerLength),
                                strokeWidth = strokeWidth
                            )
                            drawLine(
                                color = Color(0xFF355070),
                                start = androidx.compose.ui.geometry.Offset(size.width, size.height),
                                end = androidx.compose.ui.geometry.Offset(size.width - cornerLength, size.height),
                                strokeWidth = strokeWidth
                            )
                            drawLine(
                                color = Color(0xFF355070),
                                start = androidx.compose.ui.geometry.Offset(size.width, size.height),
                                end = androidx.compose.ui.geometry.Offset(size.width, size.height - cornerLength),
                                strokeWidth = strokeWidth
                            )
                        }

                        // Imagem do código de barras dentro das bordas arredondadas
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.mipmap.barcode_image),
                                contentDescription = "Código de Barras",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            )

                            Text(
                                text = barcodeNumber,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .background(Color.White)
                                    .width(200.dp)
                                    .padding(3.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                // Botão para ler o código
                Button(
                    onClick = { isScanning = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(35.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF355070))
                ) {
                    Text(
                        text = "Ler Referência",
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                // Botão para digitar o código
                Button(
                    onClick = { /* Ação de digitar código */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(35.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF355070))
                ) {
                    Text(
                        text = "Buscar",
                        color = Color.White
                    )
                }

                // Seção de informações
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Informações",
                            fontSize = 19.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Linha de Inputs - Código e Nome
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp) // espaçamento entre os campos
                        ) {
                            InfoTextField(label = "Código", modifier = Modifier.weight(1f))
                            InfoTextField(label = "Nome", modifier = Modifier.weight(1f))
                        }

                        // Linha de Inputs - Modelo e Preço
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoTextField(label = "Modelo", modifier = Modifier.weight(1f))
                            InfoTextField(label = "Preço", modifier = Modifier.weight(1f))
                        }

                        // Linha de Inputs - Tamanho e Cor
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoTextField(label = "Tamanho", modifier = Modifier.weight(1f))
                            InfoTextField(label = "Cor", modifier = Modifier.weight(1f))
                        }

                        // Linha de Inputs - Loja e Quantidade
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoTextField(label = "Loja", modifier = Modifier.weight(1f))
                            InfoTextField(label = "Quantidade", modifier = Modifier.weight(1f))
                        }
                    }
                }

                // Botão Adicionar Produto
                Button(
                    onClick = { /* Ação de adicionar produto */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(35.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF355070))
                ) {
                    Text(
                        text = "Adicionar Produto",
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun InfoTextField(label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.Black)
        BasicTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .border(1.dp, Color(0xFF355070), RoundedCornerShape(5.dp))
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BipScreenPreview() {
    MyStockAppTheme {
        MenuDrawer(titulo = "Busca") {
            Screen()
        }
    }
}