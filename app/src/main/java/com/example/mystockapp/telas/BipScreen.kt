package com.example.mystockapp.telas

import android.Manifest
import android.content.Context
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.lojaApi.LojaService
import com.example.mystockapp.api.produtoApi.CorService
import com.example.mystockapp.api.produtoApi.ModeloService
import com.example.mystockapp.api.produtoApi.ProdutoService
import com.example.mystockapp.api.produtoApi.TamanhoService
import com.example.mystockapp.models.produtos.ProdutoEdit
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.telas.viewModels.PreVendaViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject

import com.example.mystockapp.modais.ConfirmacaoDialog
import com.example.mystockapp.modais.SucessoDialog
import com.example.mystockapp.modais.componentes.SelectField
import com.example.mystockapp.models.lojas.Loja
import com.example.mystockapp.models.produtos.Cor
import com.example.mystockapp.models.produtos.ItemPromocional
import com.example.mystockapp.models.produtos.Modelo
import com.example.mystockapp.models.produtos.ProdutoCreate
import com.example.mystockapp.models.produtos.Tamanho
import com.google.gson.Gson

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
    var barcodeNumber by remember { mutableStateOf("ABC1234567890") }
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

    var modeloObj by remember { mutableStateOf(Modelo(-1, "", "", "")) }
    var tamanhoObj by remember { mutableStateOf(Tamanho(-1, -1)) }
    var corObj by remember { mutableStateOf(Cor(-1, "")) }
    var modelosOptions by remember { mutableStateOf<List<Modelo>>(emptyList()) }
    var coresOptions by remember { mutableStateOf<List<Cor>>(emptyList()) }
    var tamanhosOptions by remember { mutableStateOf<List<Tamanho>>(emptyList()) }

    val etp by viewModel.etp.observeAsState()

    // contexto local (a tela atual)
    val contexto = LocalContext.current
    var contextoBusca = if(viewModel.contextoBusca.value == "") contextoBusca else viewModel.contextoBusca.value
    Log.d("ETP-BipScreen", "Contexto passado: ${contextoBusca}")

    var loja by remember { mutableStateOf(Loja()) }
    val gson = Gson()
    val sharedPreferences = contexto.getSharedPreferences("MyStockPrefs", Context.MODE_PRIVATE)
    val idLoja = sharedPreferences.getInt("idLoja", -1) // -1 é o valor padrão caso não encontre

    var existeProduto by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSucessoDialog by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var confirmarTitulo by remember { mutableStateOf("") }
    var confirmarImagem by remember { mutableStateOf<Int?>(null) }
    var actionToPerform by remember { mutableStateOf<suspend () -> Unit>({}) } // Variável para armazenar a ação
    var confirmarBtnTitulo by remember { mutableStateOf("") }
    var recusarBtnTitulo by remember { mutableStateOf("") }
    var bgCorBtn by remember { mutableStateOf(Color(0xFF355070)) }
    var imgCasoDeErro by remember { mutableStateOf<Int?>(null) }

    // Função para atualizar o modelo, cor e tamanho após o cadastro
    fun updateObjToVar(novoModelo: String, novaCor: String, novoTamanho: Int) {
        modelo = novoModelo
        cor = novaCor
        tamanho = novoTamanho
    }

    LaunchedEffect(barcodeNumber) {
        if (barcodeNumber.isNotEmpty()) {
            println(contexto.getString(R.string.buscando_etp_por_codigo, barcodeNumber))
            viewModel.buscarEtpPorCodigo(barcodeNumber)
        }
    }

    LaunchedEffect(etp) {
        existeProduto = true
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
            itemPromocional = it.itemPromocional?.equals("SIM") ?: false
        } ?: run {
            existeProduto = false
            Toast.makeText(
                contexto,
                contexto.getString(R.string.produto_nao_existe, barcodeNumber),
                Toast.LENGTH_SHORT
            ).show()
            println(contexto.getString(R.string.etp_e_nulo))
            codigo = barcodeNumber ?: "0"
            // Limpando os campos
            nome = ""
            modelo = ""
            precoCusto = 0.0
            precoRevenda = 0.0
            tamanho =  0
            cor = ""
            quantidadeEstoque = 0
            itemPromocional = false
            quantidadeVenda = 0
        }
    }

    Log.d("ETP-BipScreen", contexto.getString(R.string.etp_bip_screen, etp))

    if (contextoBusca == "estoque" && !existeProduto) {
        LaunchedEffect(Unit) {
            val modeloSerivce = ModeloService(RetrofitInstance.modeloApi)
            val tamanhoService = TamanhoService(RetrofitInstance.tamanhoApi)
            val corService = CorService(RetrofitInstance.corApi)
            val lojaService = LojaService(RetrofitInstance.lojaApi)
            try {
                modelosOptions = modeloSerivce.fetchModelos()
                tamanhosOptions = tamanhoService.fetchTamanhos()
                coresOptions = corService.fetchCores()
                loja = lojaService.fetchLojaById(idLoja);
            } catch (e: ApiException) {
                errorMessage = "${e.message}"
            } catch (e: NetworkException) {
                errorMessage = "Network Error: ${e.message}"
            } catch (e: GeneralException) {
                errorMessage = "${e.message}"
            }
        }
    }

    // Função que controla a exibição do modal
    fun handleAbrirModalConfirm(
        titulo: String,
        imagemResId: Int,
        action: suspend () -> Unit,
        confirmarTexto: String,
        recusarTexto: String,
        corBtn: Color
    ) {
        confirmarTitulo = titulo
        confirmarImagem = imagemResId
        showConfirmDialog = true
        actionToPerform = action
        confirmarBtnTitulo = confirmarTexto
        recusarBtnTitulo = recusarTexto
        bgCorBtn = corBtn
    }

    suspend fun handleSaveProduto(
        codigo: String,
        cor: Cor,
        modelo: Modelo,
        tamanho: Tamanho,
        nome: String,
        loja: Loja,
        valorCusto: Double,
        valorRevenda: Double,
        itemPromocional: Boolean,
        quantidade: Int
    ) {
        val produtoService = ProdutoService(RetrofitInstance.produtoApi)
        try {
            val objeto = ProdutoCreate(
                codigo = codigo,
                idCor = cor.id,
                idModelo = modelo.id,
                nome = nome,
                valorCusto = valorCusto,
                valorRevenda = valorRevenda,
                tamanho = tamanho.numero,
                itemPromocional = if (itemPromocional) "SIM" else "NAO",
                idLoja = loja.id,
                quantidade = quantidade
            )
            Log.e("NovoProdutoDialog", "OBJETO DE INPUT: ${gson.toJson(objeto)}")
            val response = produtoService.createProduto(objeto)

            confirmarTitulo = "Produto salvo com sucesso"
            imgCasoDeErro = R.mipmap.ic_sucesso

//            handleAbrirModalConfirm(
//                titulo= confirmarTitulo,
//                imagemResId = R.mipmap.ic_sucesso,
//                action = { showSucessoDialog = true },
//                confirmarTexto = "Ok",
//                recusarTexto = "",
//                corBtn = Color(0xFF355070)
//            )

            updateObjToVar(modelo.nome, cor.nome, tamanho.numero)

            confirmarTitulo = "Produto salvo com sucesso"
            imgCasoDeErro = R.mipmap.ic_sucesso
            showSucessoDialog = true
            showConfirmDialog = false
            viewModel.buscarEtpPorCodigo(objeto.codigo)
            existeProduto = true
        } catch (e: ApiException) {
            existeProduto = false
            showConfirmDialog = false
            Log.e("NovoProdutoDialog", "ApiException: ${e.message}")
            val errorMessages = mutableListOf<String>()

            val jsonObject = JSONObject(e.message)

            if (jsonObject.get("message") != "" || jsonObject.get("message") != null) {
                errorMessages.add(jsonObject.get("message").toString())
                imgCasoDeErro = R.mipmap.ic_excluir
            } else {
                errorMessages.add("Erro inesperado, tente novamente")
                imgCasoDeErro = R.mipmap.ic_excluir
            }
            when (e.code) {
                201 -> {
                    Log.d("NovoProdutoDialog", "Produto salvo com sucesso")
                    confirmarTitulo = "Produto salvo com sucesso"
                    imgCasoDeErro = R.mipmap.ic_sucesso
                    existeProduto = true
                }

                400 -> {
                    errorMessage = errorMessages.joinToString("\n")
                    confirmarTitulo = errorMessages.joinToString("\n")
                    imgCasoDeErro = R.mipmap.ic_excluir
                }

                500 -> {
                    errorMessage = "Erro inesperado, tente novamente"
                    confirmarTitulo =
                        "Erro inesperado, tente novamente ou entre em contato com o suporte"
                    imgCasoDeErro = R.mipmap.ic_excluir
                }

                409 -> {
                    Log.e("NovoProdutoDialog", "ERRO 409 - ${errorMessages.joinToString("\n")}")
                    errorMessage = errorMessages.joinToString("\n")
                    confirmarTitulo = errorMessages.joinToString("\n")
                    imgCasoDeErro = R.mipmap.ic_excluir
                }

                else -> {
                    Log.e("NovoProdutoDialog", "Erro ao salvar produto: ${e.message}")
                }
            }
            showSucessoDialog = true
        }catch (e: NetworkException) {
            Log.e("NovoProdutoDialog", "NetworkException: ${e.message}")
            existeProduto = false
        } catch (e: GeneralException) {
            Log.e("NovoProdutoDialog", "GeneralException: ${e.message}")
            existeProduto = false
        }
    }

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
                            .padding(10.dp),
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
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth()
                                .padding(0.dp, vertical = 0.dp),
                            horizontalArrangement = Arrangement.Start
                        ){
                            Text(
                                text = contexto.getString(R.string.informacoes),
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.W500,
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 10.dp)
                            )
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        )
                        {
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                            ) {

                                Spacer(modifier = Modifier.height(8.dp))

                                if (contextoBusca == "pesquisa" || contextoBusca == "estoque" || contextoBusca == "pre-venda") {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        InfoTextField(
                                            label = contexto.getString(R.string.codigo),
                                            value = codigo,
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
                                        if (contextoBusca == "estoque" && !existeProduto){
                                            SelectField(
                                                label = stringResource(id = R.string.modelo),
                                                selectedOption = if (modeloObj.nome != "") modeloObj.nome else stringResource(id = R.string.select_option_label2),
                                                options = modelosOptions.map { it.nome },
                                                onOptionSelected = { modeloNome ->
                                                    val selectedModelo = modelosOptions.find { it.nome == modeloNome }
                                                    selectedModelo?.let { modeloObj = it }
                                                },
                                                error = showError && modeloObj.id == -1,
                                                labelFontSize = 16.sp,
                                                fieldHeight = 34.dp,
                                                width = 180.dp
                                            )
                                            SelectField(
                                                label = stringResource(id = R.string.cor),
                                                selectedOption = if(corObj.nome != "") corObj.nome else stringResource(id = R.string.select_option_label2),
                                                options = coresOptions.map { it.nome },
                                                onOptionSelected = { corSelected ->
                                                    val selectedCor = coresOptions.find { it.nome == corSelected }
                                                    selectedCor?.let { corObj = it }
                                                },
                                                error = showError && corObj.id == -1,
                                                labelFontSize = 16.sp,
                                                fieldHeight = 34.dp,
                                                width = 180.dp
                                            )
                                        } else {
                                            InfoTextField(
                                                label = contexto.getString(R.string.modelo),
                                                value = modelo,
                                                onValueChange = { modelo = it },
                                                editable = false,
                                                modifier = Modifier.weight(1f)
                                            )
                                            InfoTextField(
                                                label = contexto.getString(R.string.cor),
                                                value = cor,
                                                onValueChange = { cor = it },
                                                editable = false,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        if (contextoBusca == "estoque" && !existeProduto){
                                            SelectField(
                                                label = stringResource(id = R.string.tamanho),
                                                selectedOption = if (tamanhoObj.numero == -1) stringResource(id = R.string.select_option_label2) else tamanhoObj.numero.toString(),
                                                options = tamanhosOptions.map { it.numero.toString() },
                                                onOptionSelected = { tamanhoNome ->
                                                    val selectedTamanho = tamanhosOptions.find { it.numero.toString() == tamanhoNome }
                                                    selectedTamanho?.let { tamanhoObj = it }
                                                },
                                                error = showError && tamanhoObj.id == -1,
                                                labelFontSize = 16.sp,
                                                fieldHeight = 34.dp,
                                                width = 180.dp
                                            )
                                        } else {
                                            InfoTextField(
                                                label = contexto.getString(R.string.tamanho),
                                                value = tamanho.toString(),
                                                onValueChange = { newValue ->
                                                    tamanho = newValue.toIntOrNull()
                                                        ?: 0 // Conversão  para inteiros
                                                },
                                                editable = false,
                                                modifier = Modifier.weight(1f),
                                                isNumeric = true
                                            )
                                        }
                                        InfoTextField(
                                            label = contexto.getString(R.string.quantidade_est),
                                            value = quantidadeEstoque.toString(),
                                            onValueChange = { newValue ->
                                                quantidadeEstoque = newValue.toIntOrNull() ?: 0 // Conversão para inteiros
                                            },
                                            editable = contextoBusca != "pre-venda",
                                            modifier = Modifier.weight(1f),
                                            isNumeric = true
                                        )
                                    }

                                    if (contextoBusca == "pre-venda") {
                                        InfoTextField(
                                            label = contexto.getString(R.string.quantidade_venda),
                                            value = quantidadeVenda.toString(),
                                            onValueChange = {
                                                val newValue = it.toIntOrNull() ?: 0
                                                if (newValue >= 0 || newValue <=quantidadeEstoque) {
                                                    quantidadeVenda = newValue
                                                }
                                            },
                                            editable = true,
                                            modifier = Modifier.fillMaxWidth(),
                                            isNumeric = true
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
                                                onValueChange = { newValue ->
                                                    // Substitua vírgula por ponto antes da conversão para evitar problemas
                                                    val formattedValue = newValue.replace(',', '.')
                                                    precoCusto = formattedValue.toDoubleOrNull() ?: 0.0 // Conversão para decimais
                                                },
                                                editable = contextoBusca != "pre-venda",
                                                modifier = Modifier.weight(1f),
                                                isNumeric = true
                                            )
                                            InfoTextField(
                                                label = contexto.getString(R.string.preco_revenda),
                                                value = String.format("%.2f", precoRevenda),
                                                onValueChange = { newValue ->
                                                    val formattedValue = newValue.replace(',', '.')
                                                    precoRevenda = formattedValue.toDoubleOrNull() ?: 0.0 // Conversão para decimais
                                                },
                                                editable = contextoBusca != "pre-venda",
                                                modifier = Modifier.weight(1f),
                                                isNumeric = true
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

                        Spacer(modifier = Modifier.size(10.dp))

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

                        Spacer(modifier = Modifier.height(10.dp))

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

                        if (contextoBusca == "estoque") {
                            if (existeProduto) {
                                Button(
                                    onClick = {
                                        // Validação dos campos
                                        if (codigo.isEmpty() || nome.isEmpty() || modelo.isEmpty() || cor.isEmpty() ||
                                            precoCusto <= 0.0 || precoRevenda <= 0.0 || tamanho <= 0 || quantidadeEstoque <= 0
                                        ) {
                                            Toast.makeText(
                                                contexto,
                                                contexto.getString(R.string.preencha_todos_os_campos),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            // Criando o objeto de edição
                                            val produtoEditDto = ProdutoEdit(
                                                codigo = codigo,
                                                nome = nome,
                                                valorCusto = precoCusto,
                                                valorRevenda = precoRevenda,
                                                itemPromocional = if (itemPromocional) "SIM" else "NAO",
                                                quantidade = quantidadeEstoque
                                            )

                                            // Função para realizar a ação de atualização, mas apenas quando o modal for confirmado
                                            handleAbrirModalConfirm(
                                                titulo = contexto.getString(R.string.confirm_editar_titulo),
                                                imagemResId = R.mipmap.ic_editar,
                                                action = {
                                                    coroutineScope.launch {
                                                        try {
                                                            val produtoService =
                                                                ProdutoService(RetrofitInstance.produtoApi)
                                                            val response = produtoService.editarEtp(
                                                                id,
                                                                produtoEditDto
                                                            )

                                                            // Sucesso - mostrar modal de sucesso
                                                            confirmarTitulo = contexto.getString(R.string.produto_atualizado_sucesso)
                                                            imgCasoDeErro = R.mipmap.ic_sucesso
                                                            showSucessoDialog = true
                                                        } catch (e: ApiException) {
                                                            Log.e(
                                                                "AtualizarProduto",
                                                                "ApiException: ${e.message}"
                                                            )
                                                            confirmarTitulo = "Erro ao atualizar produto"
                                                            errorMessage =
                                                                contexto.getString(R.string.erro_atualizar_produto)
                                                            imgCasoDeErro =
                                                                R.mipmap.ic_excluir // Ou qualquer imagem de erro
                                                            showError = true
                                                        } catch (e: NetworkException) {
                                                            Log.e(
                                                                "AtualizarProduto",
                                                                "NetworkException: ${e.message}"
                                                            )
                                                            confirmarTitulo = "Erro ao atualizar produto, verifique a rede!"
                                                            errorMessage =
                                                                contexto.getString(R.string.erro_conexao)
                                                            imgCasoDeErro = R.mipmap.ic_excluir
                                                            showError = true
                                                        } catch (e: Exception) {
                                                            confirmarTitulo = "Erro ao atualizar produto"
                                                            Log.e(
                                                                "AtualizarProduto",
                                                                "Exception: ${e.message}"
                                                            )
                                                            errorMessage =
                                                                contexto.getString(R.string.erro_inesperado)
                                                            imgCasoDeErro = R.mipmap.ic_excluir
                                                            showError = true
                                                        }
                                                    }
                                                },
                                                confirmarTexto = contexto.getString(R.string.editar_confirm_button),
                                                recusarTexto = contexto.getString(R.string.cancel_button),
                                                corBtn = Color(0xFFBEA54C)
                                            )
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
                                        text = contexto.getString(R.string.atualizar_produto),
                                        color = Color.White
                                    )
                                }
                            } else {

//                            Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = {
                                        if (codigo.isEmpty() || modeloObj.id == -1 && tamanhoObj.id == -1 || nome.isEmpty() ||
                                            precoCusto <= 0.0 || precoRevenda <= 0.0 || corObj.id == -1 || quantidadeEstoque <= 0
                                        ) {
                                            Toast.makeText(
                                                contexto,
                                                contexto.getString(R.string.preencha_todos_os_campos),
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        } else {
                                            coroutineScope.launch {
                                                handleSaveProduto(
                                                    codigo,
                                                    corObj,
                                                    modeloObj,
                                                    tamanhoObj,
                                                    nome,
                                                    loja,
                                                    precoCusto.toDouble(),
                                                    precoRevenda.toDouble(),
                                                    itemPromocional,
                                                    quantidadeEstoque.toInt()
                                                )
                                            }
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
                                        text = contexto.getString(R.string.cadastrar_produto),
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        }

    if (showConfirmDialog) {
        ConfirmacaoDialog(
            titulo = confirmarTitulo,
            confirmarBtnTitulo = confirmarBtnTitulo,
            recusarBtnTitulo = recusarBtnTitulo,
            imagem = painterResource(id = confirmarImagem!!),
            onConfirm = {
                coroutineScope.launch {
                    try {
                        actionToPerform()
                        showConfirmDialog = false
                        showSucessoDialog = true
                    } catch (e: Exception) {
                        Log.e("InformacoesProdutoDialog", "Erro ao executar ação: ${e.message}")
                        imgCasoDeErro = R.mipmap.ic_excluir
                    }
                }
            },
            onDismiss = {
                showConfirmDialog = false
            },
            btnConfirmColor = bgCorBtn
        )
    }

    if (showSucessoDialog) {
        SucessoDialog(
            titulo = confirmarTitulo,
            onDismiss = {
                showSucessoDialog = false
//                onDismissRequest()
            },
            onConfirm = {
                showSucessoDialog = false
//                onDismissRequest()
            },
            btnConfirmColor = Color(0xFF355070),
            imagem = imgCasoDeErro?.let { painterResource(id = it) } ?: painterResource(id = R.mipmap.ic_sucesso),
            btnConfirmTitulo = "OK"
        )
    }
}

@Composable
fun InfoTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    editable: Boolean,
    modifier: Modifier = Modifier,
    isNumeric: Boolean = false
) {
    val backgroundColor = if (editable) Color.White else Color(0xFFE7E7E7)

    Column(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.Black)
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                // Permite dígitos, ponto ou vírgula para números decimais
                if (!isNumeric || newValue.matches(Regex("^\\d*[.,]?\\d*\$"))) {
                    onValueChange(newValue)
                }
            },
            enabled = editable,
            textStyle = TextStyle(color = if (editable) Color.Black else Color.DarkGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .background(backgroundColor)
                .border(1.dp, Color(0xFF355070), RoundedCornerShape(5.dp))
                .padding(8.dp),
            keyboardOptions = if (isNumeric) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BipScreenPreview() {
    val navController = rememberNavController() // Instância para o NavHostController
    val preVendaViewModel = PreVendaViewModel(1) // Supondo que você tenha um construtor padrão ou um mock
    val etpViewModel = EtpViewModel(RetrofitInstance.etpApi, LocalContext.current) // Da mesma forma, um construtor ou mock

    MyStockAppTheme {
        Screen(
            contextoBusca = "pesquisa", // pode ser pesquisa, estoque ou pre-venda
            navController = navController,
            preVendaViewModel = preVendaViewModel,
            viewModel = etpViewModel
        )
    }
}