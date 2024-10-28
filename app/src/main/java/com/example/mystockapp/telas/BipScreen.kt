package com.example.mystockapp.telas

import android.Manifest
import android.content.Context
import android.content.Intent
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
import com.example.mystockapp.telas.componentes.MenuDrawer
import com.example.mystockapp.ui.theme.MyStockAppTheme
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.telas.viewModels.PreVendaViewModel
import org.json.JSONObject

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

        // pode ser pesquisa, estoque ou pre-venda
        val contextoBusca = intent.getStringExtra("contextoBusca") ?: "pesquisa"

        setContent {
            MyStockAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Screen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        navController = rememberNavController(),
                        contextoBusca = contextoBusca,
                        preVendaViewModel = PreVendaViewModel(-1)
                    )
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
    navController: NavHostController,
    preVendaViewModel: PreVendaViewModel,
    viewModel: EtpViewModel
) {
    var barcodeNumber by remember { mutableStateOf("AB1234567890") }
    var isScanning by remember { mutableStateOf(false) }

    var id by remember { mutableStateOf(0) }
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

    // contexto local (a tela atual)
    val contexto = LocalContext.current
    var contextoBusca = if(viewModel.contextoBusca.value == "") contextoBusca else viewModel.contextoBusca.value
    Log.d("ETP-BipScreen", "Contexto passado: ${contextoBusca}")

    LaunchedEffect(barcodeNumber) {
        if (barcodeNumber.isNotEmpty()) {
            println(contexto.getString(R.string.buscando_etp_por_codigo, barcodeNumber))
            viewModel.buscarEtpPorCodigo(barcodeNumber)
        }
    }

    LaunchedEffect(etp) {
        etp?.let {
            id = it.id
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
            println(contexto.getString(R.string.etp_e_nulo))
            codigo = barcodeNumber ?: "0"
        }
    }

    Log.d("ETP-BipScreen", contexto.getString(R.string.etp_bip_screen, etp))

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
                        barcodeNumber = contexto.getString(R.string.erro, error)
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
                MenuDrawer(titulo = contexto.getString(R.string.busca)) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xFFFFFFFF),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .heightIn(min = 100.dp, max = 200.dp)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.75f)
                                    .height(150.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(250.dp)
                                        .height(150.dp)
                                        .background(Color.Transparent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val strokeWidth = 4.dp.toPx()
                                        val cornerLength = 40.dp.toPx()

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
                                            end = Offset(size.width - cornerLength, 0f),
                                            strokeWidth = strokeWidth
                                        )
                                        drawLine(
                                            color = Color(0xFF355070),
                                            start = Offset(size.width, 0f),
                                            end = Offset(size.width, cornerLength),
                                            strokeWidth = strokeWidth
                                        )

                                        drawLine(
                                            color = Color(0xFF355070),
                                            start = Offset(0f, size.height),
                                            end = Offset(cornerLength, size.height),
                                            strokeWidth = strokeWidth
                                        )
                                        drawLine(
                                            color = Color(0xFF355070),
                                            start = Offset(0f, size.height),
                                            end = Offset(0f, size.height - cornerLength),
                                            strokeWidth = strokeWidth
                                        )
                                        drawLine(
                                            color = Color(0xFF355070),
                                            start = Offset(size.width, size.height),
                                            end = Offset(size.width - cornerLength, size.height),
                                            strokeWidth = strokeWidth
                                        )
                                        drawLine(
                                            color = Color(0xFF355070),
                                            start = Offset(size.width, size.height),
                                            end = Offset(size.width, size.height - cornerLength),
                                            strokeWidth = strokeWidth
                                        )
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.mipmap.barcode_image),
                                            contentDescription = contexto.getString(R.string.codigo_de_barras),
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
                                                .border(
                                                    1.dp,
                                                    Color(0xFF355070),
                                                    RoundedCornerShape(3.dp)
                                                )
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
                                    text = contexto.getString(R.string.informacoes),
                                    fontSize = 19.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                if (contextoBusca == "pesquisa" || contextoBusca == "estoque" || contextoBusca == "pre-venda") {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        InfoTextField(
                                            label = contexto.getString(R.string.codigo),
                                            value = codigo.toString(),
                                            onValueChange = { codigo = it },
                                            editable = contextoBusca != "pre-venda",
                                            modifier = Modifier.weight(1f)
                                        )
                                        InfoTextField(
                                            label = contexto.getString(R.string.nome),
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
                                            label = contexto.getString(R.string.modelo),
                                            value = modelo,
                                            onValueChange = { modelo = it },
                                            editable = contextoBusca == "estoque",
                                            modifier = Modifier.weight(1f)
                                        )
                                        InfoTextField(
                                            label = contexto.getString(R.string.cor),
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
                                            label = contexto.getString(R.string.tamanho),
                                            value = tamanho.toString(),
                                            onValueChange = { tamanho = it.toInt() },
                                            editable = contextoBusca == "estoque",
                                            modifier = Modifier.weight(1f)
                                        )
                                        InfoTextField(
                                            label = contexto.getString(R.string.quantidade_est),
                                            value = quantidadeEstoque.toString(),
                                            onValueChange = { quantidadeEstoque = it.toInt() },
                                            editable = contextoBusca != "pre-venda",
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    if (contextoBusca == "pre-venda") {
                                        InfoTextField(
                                            label = contexto.getString(R.string.quantidade_venda),
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
                                                label = contexto.getString(R.string.preco_custo),
                                                value = String.format("%.2f", precoCusto),
                                                onValueChange = { precoCusto = it.toDouble() },
                                                editable = contextoBusca != "pre-venda",
                                                modifier = Modifier.weight(1f)
                                            )
                                            InfoTextField(
                                                label = contexto.getString(R.string.preco_revenda),
                                                value = String.format("%.2f", precoRevenda),
                                                onValueChange = { precoRevenda = it.toDouble() },
                                                editable = contextoBusca != "pre-venda",
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(0.dp)
                                    ) {
                                        Checkbox(
                                            checked = itemPromocional,
                                            onCheckedChange = { itemPromocional = it },
                                            modifier = Modifier
                                                .align(Alignment.Top)
                                                .padding(0.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = contexto.getString(R.string.item_promocional),
                                            fontSize = 16.sp,
                                            color = Color.Black,
                                            modifier = Modifier
                                                .align(Alignment.CenterVertically)
                                                .padding(start = 0.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))

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
                                text = contexto.getString(R.string.ler_referencia),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Button(
                            onClick = { viewModel.buscarEtpPorCodigo(barcodeNumber) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(35.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF355070))
                        ) {
                            Text(
                                text = contexto.getString(R.string.buscar_ref_digitada),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        if (contextoBusca == "pre-venda") {
                            Button(
                                onClick = {
                                    if (codigo.isEmpty() || nome.isEmpty() || modelo.isEmpty() || cor.isEmpty() ||
                                        precoCusto <= 0.0 || precoRevenda <= 0.0 || tamanho <= 0 || quantidadeEstoque <= 0
                                    ) {

                                        Toast.makeText(
                                            contexto,
                                            contexto.getString(R.string.preencha_todos_os_campos),
                                            Toast.LENGTH_SHORT
                                        ).show()

                                } else if (quantidadeVenda <= 0){
                                    Toast.makeText(
                                        contexto,
                                        contexto.getString(R.string.informe_quantidade),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val produto =
                                        preVendaViewModel.pesquisarNoCarrinho(id) ?:
                                        ProdutoTable(
                                        id = id,
                                        codigo = codigo,
                                        nome = nome,
                                        modelo = modelo,
                                        preco = precoRevenda,
                                        tamanho = tamanho,
                                        cor = cor,
                                        quantidade = quantidadeEstoque,
                                        quantidadeToAdd = 0,
                                        valorDesconto = 0.0
                                        )
                                    preVendaViewModel.escolherProduto(produto)
                                    preVendaViewModel.adicionar(quantidadeVenda)
                                    preVendaViewModel.desescolherProduto()
                                    navController.navigate("pre_venda")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(35.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF355070))
                        ) {
                            Text(
                                text = contexto.getString(R.string.adicionar_produto),
                                color = Color.White
                            )
                        }
                    }

                        Spacer(modifier = Modifier.height(15.dp))
                    }
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