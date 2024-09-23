package com.example.mystockapp.modais

import ProductTable
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mystockapp.ui.theme.MyStockAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mystockapp.R
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.produtoApi.ProdutoService
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.modais.componentes.ButtonComponent
import com.example.mystockapp.models.lojas.Loja
import com.example.mystockapp.models.produtos.AdicionarEstoqueReq
import kotlinx.coroutines.launch

@Composable
fun AddProdutoEstoque(onDismissRequest: () -> Unit) {
    var products by remember { mutableStateOf(listOf<ProdutoTable>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var produtosAlterados by remember { mutableStateOf(listOf<ProdutoTable>()) }

    val coroutineScope = rememberCoroutineScope()
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSucessoDialog by remember { mutableStateOf(false) }
    var imgCasoDeErro by remember { mutableStateOf<Int?>(null) }
    var actionToPerform by remember { mutableStateOf<suspend () -> Unit>({}) }
    var sucessoDialogTitulo by remember { mutableStateOf<String?>(null) }

    var tituloFinalizacaoDialog by remember { mutableStateOf<String?>(null) }

    fun handleAbrirModalConfirm(
        titulo: String,
        action: suspend () -> Unit,
        confirmarTexto: String,
        recusarTexto: String,
        corBtn: Color
    ) {
        showConfirmDialog = true
        actionToPerform = action
        sucessoDialogTitulo = titulo
    }

    fun addProduto(produto: ProdutoTable) {
        var produtoBuscado = produtosAlterados.find { it.id == produto.id }
        if (produtoBuscado in produtosAlterados) {
            produtoBuscado?.quantidadeToAdd = (produtoBuscado?.quantidadeToAdd?.plus(1) ?: 1)
        } else {
            produto.quantidadeToAdd = 1;
            produtosAlterados = produtosAlterados.toMutableList().apply { add(produto) }
        }
    }


    fun removerProduto(produto: ProdutoTable) {
        var produtoBuscado = produtosAlterados.find { it.id == produto.id }

        if (produtoBuscado in produtosAlterados) {
            if (produtoBuscado?.quantidadeToAdd?.toInt()!! >= 1) {
                produtoBuscado?.quantidadeToAdd = (produtoBuscado?.quantidadeToAdd?.toInt()?.minus(1) ?: 1)
            } else {
                produtosAlterados = produtosAlterados.toMutableList().apply { remove(produto) }
            }
        }
    }

    fun limparProdutos() {
        produtosAlterados = listOf()
    }

    LaunchedEffect(Unit) {
        val produtoService = ProdutoService(RetrofitInstance.produtoApi)
        try {
            products = produtoService.fetchProdutosTabela(1)
        } catch (e: ApiException) {
            errorMessage = "${e.message}"
        } catch (e: NetworkException) {
            errorMessage = "Network Error: ${e.message}"
        } catch (e: GeneralException) {
            errorMessage = "${e.message}"
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(8.dp)
                .border(0.dp, Color.Transparent, RoundedCornerShape(10.dp))
        ) {
            ModalHeaderComponent(onDismissRequest = onDismissRequest, "Add Produto no Estoque")
            Spacer(modifier = Modifier.height(6.dp))

            if (errorMessage != null) {
                Text(text = errorMessage ?: "", color = Color.Red)
            } else {
                ProductTable(
                    products = products,
                    onAddProduto = ::addProduto,
                    onRemoverProduto = ::removerProduto
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ButtonComponent(
                    titulo = "Limpar",
                    onClick = { limparProdutos() },
                    containerColor = Color(0xFF919191),
                )
                Spacer(modifier = Modifier.width(24.dp))
                ButtonComponent(
                    titulo = "Adicionar",
                    onClick = {
                        handleAbrirModalConfirm(
                            "Tem certeza que deseja adicionar esses produtos ao estoque?",
                            { handleAdicionarProdutos(produtosAlterados, 1) },
                            "Adicionar",
                            "Cancelar",
                            Color(0xFF355070)
                        )

                    },
                    containerColor = Color(0xFF355070),
                )
            }
        }

    }

    if (showConfirmDialog) {
        ConfirmacaoDialog(
            titulo = sucessoDialogTitulo ?: "",
            confirmarBtnTitulo = "Adicionar",
            recusarBtnTitulo = "Cancelar",
            imagem = painterResource(id = R.mipmap.ic_editar),
            onConfirm = {
                coroutineScope.launch {
                    try {
                        actionToPerform()
                        showConfirmDialog = false
                        showSucessoDialog = true

                    } catch (e: Exception) {
                        Log.e("InformacoesProdutoDialog", "Erro ao executar ação: ${e.message}")
                        imgCasoDeErro = R.mipmap.ic_excluir
                        sucessoDialogTitulo = "Erro ao adicionar produtos"
                        showConfirmDialog = false
                        showSucessoDialog = true
                    }
                }
            },
            onDismiss = {
                showConfirmDialog = false
            }
        )
    }

    if (showSucessoDialog) {
        SucessoDialog(
            titulo = tituloFinalizacaoDialog ?: "Produtos adicionados com sucesso!",
            onDismiss = {
                showSucessoDialog = false
                onDismissRequest()
            },
            onConfirm = {
                showSucessoDialog = false
                onDismissRequest()
            },
            btnConfirmColor = Color(0xFF355070),
            imagem = imgCasoDeErro?.let { painterResource(id = it) } ?: painterResource(id = R.mipmap.ic_sucesso),
            btnConfirmTitulo = "OK"
        )
    }
}
suspend fun handleAdicionarProdutos(produtosAlterados: List<ProdutoTable>, idLoja: Int) {
    val produtoService = ProdutoService(RetrofitInstance.produtoApi)
    val estoqueReqList = produtosAlterados
        .filter { produto -> produto.quantidadeToAdd > 0 }
        .map { produto ->
            AdicionarEstoqueReq(
                idEtp = produto.id,
                quantidade = produto.quantidadeToAdd
            )
        }
    try {
        produtoService.addProdutosEstoque(estoqueReqList, idLoja)
    } catch (e: ApiException) {
        throw e
    } catch (e: NetworkException) {
        throw e
    } catch (e: GeneralException) {
        throw e
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyStockAppTheme() {
        AddProdutoEstoque(onDismissRequest = {})
    }
}