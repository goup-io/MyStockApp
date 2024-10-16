package com.example.mystockapp.telas

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.mystockapp.R
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.produtoApi.EtpViewModel
import com.example.mystockapp.api.produtoApi.EtpViewModelFactory
import com.example.mystockapp.ui.theme.MyStockAppTheme
import android.util.Log


class BipScreen : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Handle permission result
    }

    private val viewModel: EtpViewModel by viewModels {
        EtpViewModelFactory(RetrofitInstance.etpApi, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val contextoBusca = intent.getStringExtra("contextoBusca") ?: "pesquisa"

        setContent {
            MyStockAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Screen(
                        modifier = Modifier.padding(innerPadding),
                        contextoBusca = contextoBusca,
                        viewModel = viewModel)
                }
            }
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted. Continue the action or workflow in your app.
            }
            else -> {
                // Request the permission.
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

@Composable
fun Screen(
    modifier: Modifier = Modifier,
    contextoBusca: String,
    viewModel: EtpViewModel
) {
    var barcodeNumber by remember { mutableStateOf("AB1234567890") }
    var isScanning by remember { mutableStateOf(false) }

    var codigo by remember { mutableStateOf("0") }
    var nome by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var precoCusto by remember { mutableStateOf(0.0) }
    var precoRevenda by remember { mutableStateOf(0.0) }
    var tamanho by remember { mutableStateOf(0) }
    var cor by remember { mutableStateOf("") }
    var quantidadeEstoque by remember { mutableStateOf(0) }
    var itemPromocional by remember { mutableStateOf(false) }
    var quantidadeVenda by remember { mutableStateOf(0) }

    val etp by viewModel.etp.observeAsState()

    LaunchedEffect(barcodeNumber) {
        if (barcodeNumber.isNotEmpty()) {
            println("Buscando ETP por código: $barcodeNumber")
            viewModel.buscarEtpPorCodigo(barcodeNumber)
        }
    }

    LaunchedEffect(etp) {
        etp?.let {
            codigo = it.codigo ?: "0"
            nome = it.produto ?: ""
            modelo = it.modelo ?: ""
            precoCusto = it.valorCusto ?: 0.0
            precoRevenda = it.valorRevenda ?: 0.0
            tamanho = it.tamanho ?: 0
            cor = it.cor ?: ""
            quantidadeEstoque = it.quantidade ?: 0
            itemPromocional = it.itemPromocional ?: false
        } ?: run {
            println("ETP é nulo")
            codigo = barcodeNumber ?: "0"
        }
    }


    Log.d("ETP-BipScreen", "ETP-BipScreen: $etp")
//    println("ETP-BipScreen: $etp")

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
            Row(
                modifier = Modifier
                    .background(Color(0XFF355070))
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botão Menu 3 Linhas (pode ser substituído por um ícone real)
                Text(
                    text = "≡", // Substituir por um ícone de menu
                    color = Color.White,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "Busca",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Image(
                    painter = painterResource(id = R.mipmap.ic_logo_mystock),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(50.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
//                    .height(180.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth(0.75f)
                            .height(150.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        // Container que simula o scanner com as bordas "arredondadas"
                        Box(
                            modifier = Modifier
                                .size(250.dp)

                                .height(150.dp)
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
                                    start = Offset(0f, 0f),
                                    end = Offset(cornerLength, 0f),
                                    strokeWidth = strokeWidth
                                )
                                drawLine(
                                    color = Color(0xFF355070),
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, cornerLength),
                                    strokeWidth = strokeWidth
                                )
                                drawLine(
                                    color = Color(0xFF355070),
                                    start = Offset(size.width, 0f),
                                    end = Offset(
                                        size.width - cornerLength,
                                        0f
                                    ),
                                    strokeWidth = strokeWidth
                                )
                                drawLine(
                                    color = Color(0xFF355070),
                                    start = Offset(size.width, 0f),
                                    end = Offset(
                                        size.width,
                                        cornerLength
                                    ),
                                    strokeWidth = strokeWidth
                                )

                                // Desenhando cantos inferiores
                                drawLine(
                                    color = Color(0xFF355070),
                                    start = Offset(0f, size.height),
                                    end = Offset(
                                        cornerLength,
                                        size.height
                                    ),
                                    strokeWidth = strokeWidth
                                )
                                drawLine(
                                    color = Color(0xFF355070),
                                    start = Offset(0f, size.height),
                                    end = Offset(
                                        0f,
                                        size.height - cornerLength
                                    ),
                                    strokeWidth = strokeWidth
                                )
                                drawLine(
                                    color = Color(0xFF355070),
                                    start = Offset(
                                        size.width,
                                        size.height
                                    ),
                                    end = Offset(
                                        size.width - cornerLength,
                                        size.height
                                    ),
                                    strokeWidth = strokeWidth
                                )
                                drawLine(
                                    color = Color(0xFF355070),
                                    start = Offset(
                                        size.width,
                                        size.height
                                    ),
                                    end = Offset(
                                        size.width,
                                        size.height - cornerLength
                                    ),
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

                                BasicTextField(
                                    value = barcodeNumber,
                                    onValueChange = { barcodeNumber = it },
                                    textStyle = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier
                                        .background(Color.White)
                                        .width(200.dp)
                                        .padding(3.dp)
                                        .align(Alignment.CenterHorizontally)
                                        .border(1.dp, Color(0xFF355070), RoundedCornerShape(3.dp))
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        Color(0xFFE7E7E7),
                        shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(5.dp))

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

                        // Exibir campos com base no contexto
                        if (contextoBusca == "pesquisa" || contextoBusca == "estoque" || contextoBusca == "pre-venda") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                InfoTextField(
                                    label = "Código",
                                    value = codigo.toString(),
                                    onValueChange = { codigo = it },
                                    editable = contextoBusca != "pre-venda",
                                    modifier = Modifier.weight(1f)
                                )
                                InfoTextField(
                                    label = "Nome",
                                    value = nome,
                                    onValueChange = { nome = it },
                                    editable = contextoBusca != "pre-venda",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                InfoTextField(
                                    label = "Modelo",
                                    value = modelo,
                                    onValueChange = { modelo = it },
                                    editable = contextoBusca == "estoque",
                                    modifier = Modifier.weight(1f)
                                )
                                InfoTextField(
                                    label = "Cor",
                                    value = cor,
                                    onValueChange = { cor = it },
                                    editable = contextoBusca == "estoque",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                InfoTextField(
                                    label = "Tamanho",
                                    value = tamanho.toString(),
                                    onValueChange = { tamanho = it.toInt() },
                                    editable = contextoBusca == "estoque",
                                    modifier = Modifier.weight(1f)
                                )
                                InfoTextField(
                                    label = "Quantidade Est.",
                                    value = quantidadeEstoque.toString(),
                                    onValueChange = { quantidadeEstoque = it.toInt() },
                                    editable = contextoBusca != "pre-venda",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (contextoBusca == "pre-venda") {
                                InfoTextField(
                                    label = "Quantidade venda",
                                    value = quantidadeVenda.toString(),
                                    onValueChange = {
                                        val newValue = it.toIntOrNull() ?: 0
                                        if (newValue in 0..quantidadeEstoque) {
                                            quantidadeVenda = newValue
                                        }
                                    },
                                    editable = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            if (contextoBusca != "pre-venda") {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    InfoTextField(
                                        label = "Preço Custo",
                                        value = String.format("%.2f", precoCusto),
                                        onValueChange = { precoCusto = it.toDouble() },
                                        editable = contextoBusca != "pre-venda",
                                        modifier = Modifier.weight(1f)
                                    )
                                    InfoTextField(
                                        label = "Preço Revenda",
                                        value = String.format("%.2f", precoRevenda),
                                        onValueChange = { precoRevenda = it.toDouble() },
                                        editable = contextoBusca != "pre-venda",
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth() // Ocupa toda a largura disponível
                                    .padding(0.dp), // Remove qualquer padding ao redor
//                                horizontalArrangement = Arrangement.Start // Alinhar os itens à esquerda
                            ) {
                                Checkbox(
                                    checked = itemPromocional,
                                    onCheckedChange = { itemPromocional = it },
                                    modifier = Modifier
                                        .align(Alignment.Top) // Alinha a Checkbox no topo
                                        .padding(0.dp) // Sem padding adicional
                                )
                                Spacer(modifier = Modifier.width(2.dp)) // Adiciona um pequeno espaço entre o Checkbox e o texto (ajustável)
                                Text(
                                    text = "Item Promocional",
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically) // Centralizar verticalmente
                                        .padding(start = 0.dp) // Certifique-se de que não há padding no texto
                                )
                            }
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
                    onClick = { /* Ação de digitar código */
                        viewModel.buscarEtpPorCodigo(barcodeNumber)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(35.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF355070))
                ) {
                    Text(
                        text = "Buscar Ref. Digitada",
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                // Botão Adicionar Produto
                Button(
                    onClick = { /* Ação de adicionar produto */},
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
fun InfoTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    editable: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (editable) Color.White else Color(0xFFE7E7E7)

    Column(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.Black)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = editable,
            textStyle = TextStyle(color = if (editable) Color.Black else Color.DarkGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(backgroundColor)
                .border(1.dp, Color(0xFF355070), RoundedCornerShape(5.dp))
                .padding(8.dp)
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun BipScreenPreview() {
//    MyStockAppTheme {
//        Screen(contextoBusca = "pesquisa")
//    }
//}