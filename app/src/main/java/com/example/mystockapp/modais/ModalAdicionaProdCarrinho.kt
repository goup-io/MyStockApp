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
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.telas.viewModels.PreVendaViewModel

@Composable
fun modalAddProdCarrinho(onDismissRequest: () -> Unit, viewModel: PreVendaViewModel, idLoja: Int) {

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val listProdutos = remember { mutableStateListOf<ProdutoTable>() }
    // mockando 10 itens
    for (i in 1..10) {
        listProdutos.add(ProdutoTable(i, "Produto $i", "Descrição do Produto $i", "dagira",1.0, 10, "colorido", 10+i))
    }

    LaunchedEffect(Unit) {
        viewModel.fetchProdutos()
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
                    products = listProdutos,
                    onAddProduto = { produto -> viewModel.addProduto(produto) },
                    onRemoverProduto = { produto -> viewModel.removerProduto(produto) },
                    isPreVenda = true,
                    viewModel = viewModel
                )
            }
        }
}



}

@Preview(showBackground = true)
@Composable
fun modalAddProdCarrinho() {
    MyStockAppTheme() {
        modalAddProdCarrinho(onDismissRequest = {}, viewModel = PreVendaViewModel(idLoja = 1), idLoja = 1)
    }
}