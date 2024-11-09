package com.example.mystockapp.telas

import DottedLineComponent
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.ui.theme.MyStockAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystockapp.modais.ModalAdicionar
import com.example.mystockapp.modais.ModalAdicionarDesconto
import com.example.mystockapp.modais.ModalResumoVenda
import com.example.mystockapp.modais.componentes.FormFieldRowComponent
import com.example.mystockapp.modais.componentes.FormFieldSelectRowComponent
import com.example.mystockapp.modais.componentes.utils.formatarPreco
import com.example.mystockapp.modais.modalAddProdCarrinho
import com.example.mystockapp.models.vendas.TipoVendasDataClass
import com.example.mystockapp.telas.componentes.ScreenTable
import com.example.mystockapp.telas.viewModels.PreVendaViewModel
import com.google.gson.Gson
import com.example.mystockapp.telas.componentes.MenuDrawer
import com.example.mystockapp.R
import com.example.mystockapp.modais.SucessoDialog
import com.example.mystockapp.models.produtos.ProdutoTable
import kotlinx.coroutines.launch

class PreVenda : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStockAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    PreVendaScreen()
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreVendaScreen(context: Context = androidx.compose.ui.platform.LocalContext.current) {

    val coroutineScope = rememberCoroutineScope()

    val sharedPreferences = context.getSharedPreferences("MyStockPrefs", Context.MODE_PRIVATE)
    val idLoja = sharedPreferences.getInt("idLoja", -1)

    val viewModel: PreVendaViewModel = viewModel(
        factory = PreVendaViewModel.AddProdCarrinhoViewModelFactory(idLoja = idLoja)
    )

    var showError by remember { mutableStateOf(false) }

    LaunchedEffect (Unit) {
        viewModel.getTiposVenda()
    }



    val gson = Gson()

    var codigo by remember { mutableStateOf(0) }
    var tipoVenda by remember { mutableStateOf(
        TipoVendasDataClass(
            id = 0,
            tipo = "",
            desconto = 0.0
        )
    ) }
    var isModalAdicionarDesconto by remember { mutableStateOf(false) }
    var isModalAddProdCarrinho by remember { mutableStateOf(false) }
    var isModalMaisInfo by remember { mutableStateOf(false) }
    var isDescontoProduto by remember {mutableStateOf(false)}

    var showSucessoDialog by mutableStateOf(false)


    MenuDrawer(titulo = stringResource(R.string.pre_venda)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF355070))
        ) {

            // Resumo da venda
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(172.dp)
                        .shadow(8.dp, RoundedCornerShape(8.dp))
                        .background(Color.White, RoundedCornerShape(8.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Header da Caixa
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(R.string.resumo_venda), fontSize = 16.sp)

                            Row {
                                // Botões com bordas arredondadas
                                Button(
                                    onClick = {
                                        isDescontoProduto = false
                                        isModalAdicionarDesconto = true
                                    },
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .width(65.dp)
                                        .height(25.dp),
                                    shape = RoundedCornerShape(5.dp),
                                    contentPadding = PaddingValues(0.dp) // Ajusta o padding
                                ) {
                                    Text(text = stringResource(R.string.adicionar_desconto), fontSize = 12.sp)
                                }

                                if (isModalAdicionarDesconto) {

                                    ModalAdicionarDesconto(
                                        vendaDetalhes = viewModel.vendaDetalhes,
                                        isDescontoProduto = isDescontoProduto,
                                        produtoInfo = viewModel.produtoSelecionado ?: ProdutoTable(0, "", "", "", 0.0, 0, "", 0, 0, 0.0),
                                        onDismissRequest = {
                                            viewModel.desescolherProduto()
                                            isModalAdicionarDesconto = false
                                        },
                                        onSalvarDescontoVenda = { desconto, porcentagemDesconto ->
                                            viewModel.adicionarDescontoVenda(desconto, porcentagemDesconto)
                                        },
                                        onSalvarDescontoProduto = { valorDesconto ->
                                            viewModel.adicionarDescontoProd(
                                                viewModel.produtoSelecionado ?: ProdutoTable(0, "", "", "", 0.0, 0, "", 0, 0, 0.0) ,
                                                valorDesconto
                                            )
                                        }
                                    )
                                }

                                Button(
                                    onClick = { isModalMaisInfo = true },
                                    modifier = Modifier
                                        .width(25.dp)
                                        .height(25.dp),
                                    shape = RoundedCornerShape(5.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF96BDCE) // Definindo a cor de fundo
                                    ),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(text = stringResource(R.string.mais_info), fontSize = 22.sp, color = Color.White)
                                }
                                if (isModalMaisInfo) {
                                    ModalResumoVenda(
                                        detalhes = viewModel.vendaDetalhes,
                                        onDismissRequest = { isModalMaisInfo = false }
                                    )
                                }
                            }
                        }

                        // Conteúdo da Caixa
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceAround

                        ) {
                            // Código da Venda


                            // Valor da Venda
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = stringResource(R.string.valor_venda), fontSize = 14.sp)
                                Text(text = "R$ " + formatarPreco(viewModel.vendaDetalhes.valorTotal.toString().replace(".",",")), fontSize = 14.sp)
                            }

                            DottedLineComponent()

                            // Quantidade de Itens
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = stringResource(R.string.quantidade_itens), fontSize = 14.sp)
                                Text(text = viewModel.vendaDetalhes.totalItens.toString(), fontSize = 14.sp)
                            }

                            DottedLineComponent()
                        }
                    }
                }
            }

            // Caixa grande branca (Carrinho)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE7E7E7))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(365.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Column {
                        // Header da caixa grande
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(R.string.carrinho), fontSize = 20.sp, color = Color.Black)

                            Row {
                                Button(
                                    onClick = { /* Ação do botão 1 */ },
                                    modifier = Modifier
                                        .width(65.dp)
                                        .height(25.dp),
                                    shape = RoundedCornerShape(5.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF355070)
                                    )
                                ) {
                                    Text(text = stringResource(R.string.codigo), color = Color.White, fontSize = 12.sp)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        isModalAddProdCarrinho = true
                                    },
                                    modifier = Modifier
                                        .width(70.dp)
                                        .height(25.dp),
                                    shape = RoundedCornerShape(5.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF355070)
                                    )
                                ) {
                                    Text(text = stringResource(R.string.add_prod), color = Color.White, fontSize = 12.sp)
                                }

                                if (isModalAddProdCarrinho) {
                                    modalAddProdCarrinho(
                                        onDismissRequest = {
                                            isModalAddProdCarrinho = false
                                        },
                                        viewModel
                                    )
                                }

                                if (viewModel.produtoSelecionado != null && !viewModel.abrirModalAdicionarMinimizado && !isModalAdicionarDesconto) {
                                    isModalAddProdCarrinho = false
                                    ModalAdicionar(
                                        onDismissRequest = { viewModel.desescolherProduto() },
                                        viewModel = viewModel,
                                        isPreVenda = true,
                                        onConfirmarAdd = {quantidade -> viewModel.adicionar(quantidade)},
                                        isMinimized = false,
                                        titulo = stringResource(R.string.adicionar_produto)
                                    )
                                }
                        }
                    }

                    // Tabela dentro da caixa grande
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .height(310.dp)
                            .background(Color(0xFF355070))
                            .padding(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .align(Alignment.CenterHorizontally)
                    ) {

                        ScreenTable(
                            products = viewModel.carrinho.itensCarrinho,
                            verMaisAction = { produto ->
                                viewModel.escolherProduto(produto)
                                viewModel.abrirModalAdicionarMinimizado = true
                            },
                            isPreVenda = true,
                        )
                        if (viewModel.produtoSelecionado != null && viewModel.abrirModalAdicionarMinimizado) {
                            ModalAdicionar(
                                onDismissRequest = { viewModel.desescolherProduto() },
                                viewModel = viewModel,
                                isPreVenda = true,
                                onConfirmarAdd = { quantidade -> viewModel.adicionar(quantidade) },
                                isMinimized = true,
                                titulo = stringResource(R.string.mais_informacoes),
                                abrirDesconto = {
                                    isDescontoProduto = true
                                    isModalAdicionarDesconto = true
                                    viewModel.abrirModalAdicionarMinimizado = false
                                },
                                abrirAdicionarCarrinho = {
                                    viewModel.abrirModalAdicionarMinimizado = false
                                                         },
                            )
                        }
                        }
                    }
                }

                // Caixa pequena branca com inputs
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(52.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        FormFieldRowComponent(
                            label = stringResource(R.string.codigo),
                            fieldType = KeyboardType.Number,
                            textValue = if (codigo <= 0 || codigo == null) "" else codigo.toString(),
                            onValueChange = {
                                input ->
                                val novoTexto = input
                                    .replace("\\D".toRegex(), "")
                                codigo = if (novoTexto == "") 0 else novoTexto.toInt()
                                            },
                            width = 120.dp,
                            error = showError && codigo <= 0 || codigo == null
                        )

                        FormFieldSelectRowComponent(
                            label = stringResource(R.string.tipo_venda),
                            selectedOption = tipoVenda.tipo,
                            options = viewModel.tipoVendas.map { it.tipo},
                            onOptionSelected = {
                                    tipoVendaSelected ->
                                tipoVenda = viewModel.tipoVendas.find { it.tipo == tipoVendaSelected }!!
                            },
                            width = 200.dp,
                            error = showError && tipoVenda.id <= 0 || tipoVenda == null
                        )
                    }
                }

                // Botão azul na parte inferior
                Button(
                    onClick = {
                        if( codigo <= 0 || tipoVenda.id <= 0){
                            showError = true
                        } else {
                            coroutineScope.launch {
                                try {
                                    viewModel.atualizarVendaInfo(tipoVendaId = tipoVenda.id, codigoVendedor = codigo)
                                    viewModel.realizarVenda()
                                } catch (e: Exception) {
                                    Log.e("PreVenda", "Erro ao executar ação: ${e.message}")
                                } finally {
                                    tipoVenda = TipoVendasDataClass(
                                        id = 0,
                                        tipo = "",
                                        desconto = 0.0
                                    )
                                    codigo = 0
                                }
                            }
                        } },
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF355070)
                    )
                ) {
                    Text(text = stringResource(R.string.finalizar_venda), color = Color.White, fontSize = 16.sp)
                }
                if (showSucessoDialog) {
                    SucessoDialog(
                        titulo = viewModel.sucessoDialogTitulo,
                        onDismiss = {
                            showSucessoDialog = false
                        },
                        onConfirm = {
                            showSucessoDialog = false
                        },
                        btnConfirmColor = Color(0xFF355070),
                        imagem = viewModel.imgCasoDeErro?.let { painterResource(id = it) } ?: painterResource(id = R.mipmap.ic_sucesso),
                        btnConfirmTitulo = stringResource(id = R.string.ok)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyStockAppTheme {
        PreVendaScreen()
    }
}