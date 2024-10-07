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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.produtoApi.ProdutoService
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.modais.componentes.ButtonComponent
import com.example.mystockapp.models.produtos.ProdutoQuantidadeAdd
import com.example.mystockapp.telas.viewModels.PreVendaViewModel

@Composable
fun modalAddProdCarrinho(onDismissRequest: () -> Unit, viewModel: PreVendaViewModel, idLoja: Int) {

    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                    products = viewModel.produtos,
                    onAddProduto = { produto -> viewModel.addProduto(produto) },
                    onRemoverProduto = { produto -> viewModel.removerProduto(produto) }
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
                    onClick = { viewModel.limparProdutos() },
                    containerColor = Color(0xFF919191),
                )
                Spacer(modifier = Modifier.width(24.dp))
                ButtonComponent(
                    titulo = "Adicionar",
                    onClick = {
                        viewModel.adicionar()
                        onDismissRequest()
                        },
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
        modalAddProdCarrinho(onDismissRequest = {}, viewModel = PreVendaViewModel(idLoja = 1), idLoja = 1)
    }
}