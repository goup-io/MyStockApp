package com.example.mystockapp.telas

import ProductTable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mystockapp.models.produtos.Produto
import com.example.mystockapp.telas.componentes.Header
import com.example.mystockapp.telas.componentes.Spinner
import com.example.mystockapp.telas.componentes.Table
import com.example.mystockapp.ui.theme.MyStockAppTheme
import com.example.mystockapp.ui.theme.Cores


class Estoque : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStockAppTheme {
                EstoqueScreen()

            }
        }
    }
}

@Composable
fun EstoqueScreen() {
    val scrollState = rememberScrollState()

    val produtos = listOf(
        Produto("P001", "Nike Air Force 1 Off-White", "Preto", 1299.99, "AirForce", 30, 42),
        Produto("P002", "Adidas Ultraboost 21", "Branco", 999.99, "Ultraboost", 20, 41),
        Produto("P003", "Puma RS-X3", "Azul", 749.99, "RS-X", 25, 43),
        Produto("P004", "New Balance 550 White/Navy", "Cinza", 899.99, "NB550", 15, 44),
        Produto("P005", "Reebok Club C 85", "Preto", 499.99, "ClubC", 40, 40),
        Produto("P006", "Asics Gel-Kayano 28", "Vermelho", 799.99, "Gel-Kayano", 10, 42),
        Produto("P007", "Converse Chuck Taylor All Star", "Branco", 299.99, "ChuckTaylor", 50, 39),
        Produto("P008", "Under Armour HOVR Phantom", "Verde", 899.99, "HOVR", 35, 44),
        Produto("P009", "Saucony Triumph 19", "Preto", 849.99, "Triumph", 20, 41),
        Produto("P010", "Brooks Ghost 15", "Azul Marinho", 759.99, "Ghost", 30, 43)
    )


    val optModelos = produtos.map {it.modelo}
    var selectedModelo by rememberSaveable { mutableStateOf(optModelos[0]) }

    val optCores = produtos.map { it.cor}
    var selectedCor by rememberSaveable { mutableStateOf(optCores[0]) }

    val optTamanhos = produtos.map { it.tamanho }
    var selectedTamanho by rememberSaveable { mutableStateOf(optTamanhos[0]) }

    val optPrecos = produtos.map { it.preco }
    var selectedPreco by rememberSaveable { mutableStateOf(optPrecos[0]) }

    Column(modifier = Modifier.fillMaxWidth().background(Cores.AzulBackground).verticalScroll(scrollState)) {
        Column {
            Header("Estoque", onClick = {})
        }
        Column {
            Box (modifier = Modifier.fillMaxWidth().padding(16.dp).background(color = Cores.White, shape = RoundedCornerShape(16.dp))){
                Column(modifier = Modifier.padding(16.dp)) {
                    Row (modifier = Modifier.fillMaxWidth()){
                        Column (modifier = Modifier.weight(0.45f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Modelo")
                            Spinner(itemList = optModelos, selectedItem = selectedModelo, onItemSelected = { selectedModelo = it})
                        }
                        Spacer(modifier = Modifier.weight(0.1f))
                        Column (modifier = Modifier.weight(0.45f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Cor")
                            Spinner(itemList = optCores, selectedItem = selectedCor, onItemSelected = { selectedCor = it})
                        }
                    }
                    Row (modifier = Modifier.fillMaxWidth()){
                        Column (modifier = Modifier.weight(0.45f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Tamanho")
                            Spinner(itemList = optTamanhos, selectedItem = selectedTamanho, onItemSelected = { selectedTamanho = it})
                        }
                        Spacer(modifier = Modifier.weight(0.1f))
                        Column (modifier = Modifier.weight(0.45f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Preço até")
                            Spinner(itemList = optPrecos, selectedItem = selectedPreco, onItemSelected = { selectedPreco = it})
                        }
                    }
                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
                        Column { Button(colors = androidx.compose.material.ButtonDefaults.buttonColors(Cores.PurpleGrey40), onClick = { }) { Text(text = "Limpar", color = Cores.White) } }
                        Column { Button(colors = androidx.compose.material.ButtonDefaults.buttonColors(Cores.AzulBackground), onClick = {}) { Text(text = "Filtrar", color = Cores.White) } }
                    }
                }
            }
        }
        Column (modifier = Modifier.background(Cores.Gray)){
            Box (modifier = Modifier.fillMaxWidth().padding(16.dp).background(color = Cores.White, shape = RoundedCornerShape(16.dp))){
                Table(produtos)
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
