package com.example.mystockapp.modais

import ProductTable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mystockapp.ui.theme.MyStockAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mystockapp.models.ProdutoTable
import com.example.mystockapp.modais.componentes.ButtonComponent

class ModalAdicionaProdCarrinho : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStockAppTheme() {
                modalAddProdCarrinho(onDismissRequest = {})
            }
        }
    }
}

@Composable
fun modalAddProdCarrinho(onDismissRequest: () -> Unit) {

    val products = listOf(
        ProdutoTable("Triple Black", "Air Force", "300,00", "37", "Preto",  "20"),
        ProdutoTable("Classic White", "Air Max", "400,00", "38", "Branco", "15"),
        ProdutoTable("Classic White", "Air Max", "400,00", "38", "Branco",  "15"),
        ProdutoTable("Classic White", "Air Max", "400,00", "38", "Branco", "15"),
        ProdutoTable("Classic White", "Air Max", "400,00", "38", "Branco",  "15"),
        ProdutoTable("Classic White", "Air Max", "400,00", "38", "Branco",  "15"),
        ProdutoTable("Classic White", "Air Max", "400,00", "38", "Branco",  "15"),
        ProdutoTable("Classic White", "Air Max", "400,00", "38", "Branco", "15")
    )
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(16.dp)
                .border(0.dp, Color.Transparent, RoundedCornerShape(10.dp))
        ) {
            ModalHeaderComponent(onDismissRequest = onDismissRequest, "Add Produto no Estoque")
            Spacer(modifier = Modifier.height(6.dp))

            ProductTable(products)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ButtonComponent(
                    titulo = "Excluir",
                    onClick = { onDismissRequest() },
                    containerColor = Color(0xFF919191),
                )
                Spacer(modifier = Modifier.width(24.dp))
                ButtonComponent(
                    titulo = "Editar",
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