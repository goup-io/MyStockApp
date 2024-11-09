package com.example.mystockapp.telas

import InformacoesProdutoDialog
import NovoProdutoDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystockapp.R
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.produtoApi.CorService
import com.example.mystockapp.api.produtoApi.ModeloService
import com.example.mystockapp.api.produtoApi.TamanhoService
import com.example.mystockapp.modais.AddProdutoEstoque
import com.example.mystockapp.modais.ConfirmacaoDialog
import com.example.mystockapp.modais.ModalAdicionar
import com.example.mystockapp.modais.ModalNovoModeloDialog
import com.example.mystockapp.modais.SucessoDialog
import com.example.mystockapp.modais.componentes.SelectField
import com.example.mystockapp.modais.viewModels.AddProdEstoqueViewModel
import com.example.mystockapp.telas.viewModels.EstoqueViewModel
import com.example.mystockapp.models.produtos.Cor
import com.example.mystockapp.models.produtos.Modelo
import com.example.mystockapp.models.produtos.Tamanho
import com.example.mystockapp.telas.componentes.ScreenTable
import com.example.mystockapp.telas.componentes.MenuDrawer
import com.example.mystockapp.ui.theme.MyStockAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class Estoque : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStockAppTheme {
                androidx.compose.material3.Scaffold { innerPadding ->
                    EstoqueScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }

            }
        }
    }
}

@Composable
fun EstoqueScreen(
    context: Context = androidx.compose.ui.platform.LocalContext.current,
    modifier : Modifier = Modifier
) {

    val sharedPreferences = context.getSharedPreferences("MyStockPrefs", Context.MODE_PRIVATE)
    val idLoja = sharedPreferences.getInt("idLoja", -1)

    val viewModel: EstoqueViewModel = viewModel(
        factory = EstoqueViewModel.EstoqueViewModelFactory(idLoja = idLoja)
    )

    val addProdEstoqueViewModel: AddProdEstoqueViewModel = viewModel(
        factory = AddProdEstoqueViewModel.AddProdEstoqueViewModelFactory(idLoja = idLoja)
    )

    val coroutineScope = rememberCoroutineScope()


    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var codigo by remember { mutableStateOf("") }
    var tipoVenda by remember { mutableStateOf("") }

    var isModalAddProd by remember { mutableStateOf(false) }
    var isModalNovoProd by remember { mutableStateOf(false) }
    var isModalNovoModelo by remember { mutableStateOf(false) }



    var modelo by remember { mutableStateOf(Modelo(-1, "", "", "")) }
    var tamanho by remember { mutableStateOf(Tamanho(-1, -1)) }
    var cor by remember { mutableStateOf(Cor(-1, "")) }
    var preco by remember { mutableStateOf(0.0) }

    var queryPesquisa by remember { mutableStateOf("") }

    var modelosOptions by remember { mutableStateOf<List<Modelo>>(emptyList()) }
    var coresOptions by remember { mutableStateOf<List<Cor>>(emptyList()) }
    var tamanhosOptions by remember { mutableStateOf<List<Tamanho>>(emptyList()) }
    var precoOptions by remember { mutableStateOf(listOf(50.0,100.0,200.0))}

    val contexto = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchProdutos()
        val modeloSerivce = ModeloService(RetrofitInstance.modeloApi)
        val tamanhoService = TamanhoService(RetrofitInstance.tamanhoApi)
        val corService = CorService(RetrofitInstance.corApi)
        try {
            modelosOptions = modeloSerivce.fetchModelos()
            tamanhosOptions = tamanhoService.fetchTamanhos()
            coresOptions = corService.fetchCores();
        } catch (e: ApiException) {
            errorMessage = "${e.message}"
        } catch (e: NetworkException) {
            errorMessage = "Network Error: ${e.message}"
        } catch (e: GeneralException) {
            errorMessage = "${e.message}"
        }
    }

    MenuDrawer(
        modifier = modifier,
        titulo = stringResource(id = R.string.titulo_estoque)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF355070))
        ) {
            // Função para limpar os filtros
            fun limparFiltros() {
                modelo = Modelo(-1, "", "", "")
                cor = Cor(id = -1, nome = "")
                tamanho = Tamanho(id = -1, numero = -1)
                preco = 0.0
            }

// Filtros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(175.dp)
                        .shadow(8.dp, RoundedCornerShape(8.dp))
                        .background(Color.White, RoundedCornerShape(8.dp)) // Cor de fundo da caixa
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxHeight()
                            .padding(6.dp)
                    ) {
                        // Primeira linha (label e input)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // SelectField para Modelo
                            SelectField(
                                fieldHeight = 30.dp,
                                label = stringResource(id = R.string.label_modelo),
                                selectedOption = modelo.nome,
                                options = modelosOptions.map { it.nome },
                                onOptionSelected = { modeloNome ->
                                    val selectedModelo = modelosOptions.find { it.nome == modeloNome }
                                    selectedModelo?.let { modelo = it }
                                },
                                modifier = Modifier.weight(1.4f)
                            )

                            Spacer(modifier = Modifier.width(16.dp)) // Espaço entre os grupos

                            // SelectField para Cor
                            SelectField(
                                fieldHeight = 30.dp,
                                label = stringResource(id = R.string.label_cor),
                                selectedOption = cor.nome,
                                options = coresOptions.map { it.nome },
                                onOptionSelected = { corSelected ->
                                    val selectedCor = coresOptions.find { it.nome == corSelected }
                                    selectedCor?.let { cor = it } },
                                modifier = Modifier.weight(1.4f)
                            )
                        }

                        // Segunda linha (SelectField para Tamanho e Preço)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // SelectField para Tamanho
                            SelectField(
                                fieldHeight = 30.dp,
                                label = stringResource(id = R.string.label_tamanho),
                                selectedOption = if (tamanho.numero == -1) "" else tamanho.numero.toString(),
                                options = tamanhosOptions.map { it.numero.toString() },
                                onOptionSelected = { tamanhoNome ->
                                    val selectedTamanho = tamanhosOptions.find { it.numero.toString() == tamanhoNome }
                                    selectedTamanho?.let { tamanho = it } },
                                modifier = Modifier.weight(1.4f)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // SelectField para Preço
                            SelectField(
                                fieldHeight = 30.dp,
                                label = stringResource(id = R.string.label_preco),
                                selectedOption = if(preco == 0.0) "" else preco.toString(),
                                options = precoOptions,
                                onOptionSelected = { selectedPreco ->
                                    val selectedPreco = precoOptions.find { it == selectedPreco.toDouble() }
                                    if (selectedPreco != null) {
                                        preco = selectedPreco
                                    } },
                                modifier = Modifier.weight(1.4f)
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        // Terceira linha (botões)
                        Row(
                            modifier = Modifier.fillMaxWidth(0.75f).padding(start = 90.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            // Botão Limpar
                            androidx.compose.material3.Button(
                                onClick = { limparFiltros()
                                    coroutineScope.launch {
                                        try {
                                            viewModel.fetchProdutos()
                                        } catch (e: Exception) {
                                            //deu erro
                                        }
                                    }
                                          }, // Chama a função limparFiltros ao clicar
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(32.dp),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF919191)
                                )
                            ) {
                                androidx.compose.material3.Text(
                                    text = stringResource(id = R.string.botao_limpar),
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }

                            // Botão Filtrar
                            androidx.compose.material3.Button(
                                onClick = {
                                    coroutineScope.launch {
                                        try {
                                            viewModel.buscarPorFiltros(modelo.nome,tamanho.numero,cor.nome,precoMin = 0.0,precoMax = preco)
                                        } catch (e: Exception) {
                                            //deu erro
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(32.dp),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF355070)
                                )
                            ) {
                                androidx.compose.material3.Text(
                                    text = stringResource(id = R.string.botao_filtrar),
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // Caixa grande branca (Carrinho)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE7E7E7))
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(375.dp)
                        .background(Color(0xFFE7E7E7))
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        // Header da caixa grande com campo de pesquisa
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Título "Produtos"
                            androidx.compose.material3.Text(
                                text = stringResource(id = R.string.titulo_produtos),
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.W500,
                            )

                            // Campo de pesquisa e botão
                            Row(
                                modifier = Modifier.padding(0.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {


                                // Input para pesquisa
                                Box(
                                    contentAlignment = Alignment.CenterStart, // Alinha o conteúdo verticalmente ao centro na Box
                                    modifier = Modifier
                                        .width(145.dp)
                                        .height(30.dp)
                                        .border(0.5.dp, Color.Gray, RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                                        .background(Color.White, RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                                        .padding(start = 8.dp) // Adiciona um padding à esquerda
                                        .align(alignment = Alignment.CenterVertically)
                                ) {
                                    BasicTextField(
                                        value = queryPesquisa,
                                        onValueChange = { queryPesquisa = it },
                                        singleLine = true,
                                        textStyle = androidx.compose.ui.text.TextStyle(
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Start // Alinha o texto à esquerda,
                                        ),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(vertical = 4.dp) // Adiciona padding vertical para centralizar o texto
                                    )
                                }



                                // Botão com ícone
                                androidx.compose.material3.Button(
                                    onClick = {
                                    coroutineScope.launch {
                                        try {
                                            viewModel.buscarProdutos(queryPesquisa)
                                        } catch (e: Exception) {
                                            //deu erro
                                        }
                                    }
                                },
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(30.dp)
                                        .padding(0.dp),
                                    shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF355070)
                                    )
                                ) {
                                    Icon(
//                                        painter = painterResource(id = R.mipmap.search),
                                        imageVector = Icons.Default.Search,
                                        contentDescription = stringResource(id = R.string.descricao_pesquisar),
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                    // Tabela dentro da caixa grande
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(325.dp)
                            .background(Color(0xFF355070), RoundedCornerShape(8.dp))
                            .padding(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .align(Alignment.CenterHorizontally)
                    ) {

                        ScreenTable(
                            products = viewModel.produtos,
                            verMaisAction = {
                                product ->
                                viewModel.escolherProduto(product)
                            },
                            isPreVenda = false
                        )

                        if (viewModel.produtoSelecionado != null) {
                            InformacoesProdutoDialog(
                                onDismissRequest = {
                                    viewModel.desescolherProduto()
                                    viewModel.atualizarEstoque = true
                                                   },
                                idProduto = viewModel.produtoSelecionado!!.id,
                            )
                        }

                    }
                }
            }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    androidx.compose.material3.Button(
                        onClick = {
                            val telaBip = Intent(contexto, BipScreen::class.java)
                            telaBip.putExtra("contextoBusca", "estoque")
                            contexto.startActivity(telaBip)
                                  },

                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF355070)
                        )
                    ) {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.botao_codigo),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    androidx.compose.material3.Button(
                        onClick = {
                            isModalNovoProd = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF355070)
                        )
                    ) {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.botao_novo_produto),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    if (isModalNovoProd) {
                        NovoProdutoDialog(
                            onDismissRequest = {
                                isModalNovoProd = false
                                viewModel.atualizarEstoque = true
                            }
                        )
                    }
                }

                // Dois botões azuis na parte inferior
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    androidx.compose.material3.Button(
                        onClick = {
                            isModalAddProd = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF355070)
                        )
                    ) {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.botao_add_produto),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    if (isModalAddProd) {
                        AddProdutoEstoque(
                            onDismissRequest = { isModalAddProd = false },
                        viewModel = addProdEstoqueViewModel
                    )
                }

                if (addProdEstoqueViewModel.produtoSelecionado != null) {
                    isModalAddProd = false
                    ModalAdicionar(
                        onDismissRequest = {
                            viewModel.atualizarEstoque()
                            addProdEstoqueViewModel.desescolherProduto()
                                           },
                        viewModel = addProdEstoqueViewModel,
                        isPreVenda = false,
                        onConfirmarAdd = { quantidade ->
                                addProdEstoqueViewModel.adicionarNoEstoque(quantidade)
                                viewModel.atualizarEstoque()
                           },
                        isMinimized = false,
                        titulo = stringResource(R.string.adicionar_estoque)
                    )
                }

                if (addProdEstoqueViewModel.showConfirmDialog) {
                    ConfirmacaoDialog(
                        titulo = "Alterar a quantidade desse produto no estoque?",
                        confirmarBtnTitulo = "Adicionar",
                        recusarBtnTitulo = "Cancelar",
                        imagem = painterResource(id = R.mipmap.ic_editar),
                        onConfirm = {
                            coroutineScope.launch {
                                addProdEstoqueViewModel.showConfirmDialog = false
                                addProdEstoqueViewModel.executarFuncao = true
                                addProdEstoqueViewModel.adicionar()
                            }
                        },
                        onDismiss = {
                            addProdEstoqueViewModel.showConfirmDialog = false
                        }
                    )
                }

                    if(viewModel.atualizarEstoque){
                        viewModel.atualizarEstoque()
                    }

                if (addProdEstoqueViewModel.showSucessoDialog) {
                    SucessoDialog(
                        titulo = addProdEstoqueViewModel.sucessoDialogTitulo,
                        onDismiss = {
                            addProdEstoqueViewModel.showSucessoDialog = false
                            viewModel.atualizarEstoque()
                        },
                        onConfirm = {
                            addProdEstoqueViewModel.showSucessoDialog = false
                            viewModel.atualizarEstoque()
                        },
                        btnConfirmColor = Color(0xFF355070),
                        imagem = addProdEstoqueViewModel.imgCasoDeErro?.let { painterResource(id = it) } ?: painterResource(id = R.mipmap.ic_sucesso),
                        btnConfirmTitulo = "OK")
                    }

                    androidx.compose.material3.Button(
                        onClick = {
                            isModalNovoModelo = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF355070)
                        )
                    ) {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.botao_novo_modelo),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    if (isModalNovoModelo) {
                        ModalNovoModeloDialog(
                            onDismissRequest = {
                                isModalNovoModelo = false
                                viewModel.atualizarEstoque = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEstoqueScreen() {
    MyStockAppTheme {
        EstoqueScreen()
    }
}
