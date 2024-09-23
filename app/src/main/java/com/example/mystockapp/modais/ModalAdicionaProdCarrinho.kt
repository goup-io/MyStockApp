package com.example.mystockapp.modais

import ProductTable
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.produtoApi.ProdutoService
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.modais.componentes.ButtonComponent
import com.example.mystockapp.models.produtos.ProdutoQuantidadeAdd

@Composable
fun modalAddProdCarrinho(onDismissRequest: () -> Unit) {

    var products by remember { mutableStateOf(listOf<ProdutoTable>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var produtosAlterados by remember { mutableStateOf(listOf<ProdutoTable>()) }


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

            ModalHeaderComponent(onDismissRequest = onDismissRequest, "Add Produto no Carrinho")
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
                    onClick = { onDismissRequest() },
                    containerColor = Color(0xFF919191),
                )
                Spacer(modifier = Modifier.width(24.dp))
                ButtonComponent(
                    titulo = "Adicionar",
                    onClick = {
                       onDismissRequest() },
                    containerColor = Color(0xFF355070),
                )
            }
        }
}



}

@Preview(showBackground = true)
@Composable
fun modalAddProdCarrinho() {
    MyStockAppTheme() {
        modalAddProdCarrinho(onDismissRequest = {})
    }
}