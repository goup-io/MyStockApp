package com.example.mystockapp.telas

import NovoProdutoDialog
import ProductTable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.R
import com.example.mystockapp.modais.AddProdutoEstoque
import com.example.mystockapp.modais.ModalNovoModeloDialog
import com.example.mystockapp.modais.componentes.SelectField
import com.example.mystockapp.models.produtos.Produto
import com.example.mystockapp.models.produtos.ProdutoTable
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

    var codigo by remember { mutableStateOf("") }
    var tipoVenda by remember { mutableStateOf("") }

    var isModalAddProd by remember { mutableStateOf(false) }
    var isModalNovoProd by remember { mutableStateOf(false) }
    var isModalNovoModelo by remember { mutableStateOf(false) }
    
    val products = listOf(
        ProdutoTable(0,"Triple Black", "Air Force", 300.00, 37, "Preto",  20),
        ProdutoTable(0,"Classic White", "Air Max", 400.00, 38, "Branco", 15),
        ProdutoTable(0,"Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"Classic White", "Air Max", 400.00, 38, "Branco", 15),
        ProdutoTable(0,"Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"Classic White", "Air Max", 400.00, 38, "Branco", 15)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF355070))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(52.dp)
                .padding(16.dp)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Botão de menu o
            androidx.compose.material3.Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(30.dp)
                    .height(55.dp)
                    .clip(RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // Botão com cor transparente
                    contentColor = Color.White // Cor do texto e ícones dentro do botão
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.menu),
                    contentDescription = "menu",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }



            // Título no centro
            androidx.compose.material3.Text(
                text = "Estoque",
                color = Color.White,
                fontSize = 20.sp
            )

            // Imagem da logo da empresa na direita
            Image(
                painter = painterResource(id = R.mipmap.mystock),
                contentDescription = "Logo da Empresa",
                modifier = Modifier.size(40.dp)
            )
        }

        // Filtros
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
                    .background(Color.White, RoundedCornerShape(8.dp)) // Cor de fundo da caixa
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
//                        .padding(top = 16.dp)
                ) {

                    // Primeira linha (SelectField para Mod. e Cor)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // SelectField para Mod.
                        SelectField(
                            label = "Modelo :",
                            selectedOption = "",
                            options = listOf("Modelo 1", "Modelo 2", "Modelo 3"),
                            onOptionSelected = { /* Ação ao selecionar */ },
                            modifier = Modifier.weight(1.4f)
                        )

                        Spacer(modifier = Modifier.width(16.dp)) // Espaço entre os grupos

                        // SelectField para Cor
                        SelectField(
                            label = "Cor :",
                            selectedOption = "",
                            options = listOf("Cor 1", "Cor 2", "Cor 3"),
                            onOptionSelected = { /* Ação ao selecionar */ },
                            modifier = Modifier.weight(1.4f)
                        )
                    }

//                    Spacer(modifier = Modifier.height(5.dp))


                    // Segunda linha (SelectField para Tam. e Preço)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // SelectField para Tam.
                        SelectField(
                            label = "Tamanho :",
                            selectedOption = "",
                            options = listOf("P", "M", "G"),
                            onOptionSelected = { /* Ação ao selecionar */ },
                            modifier = Modifier.weight(1.4f)
                        )

                        Spacer(modifier = Modifier.width(16.dp)) // Espaço entre os grupos

                        // SelectField para Preço
                        SelectField(
                            label = "Preço :",
                            selectedOption = "",
                            options = listOf("R$ 50", "R$ 100", "R$ 150"),
                            onOptionSelected = { /* Ação ao selecionar */ },
                            modifier = Modifier.weight(1.4f)
                        )
                    }


                    Spacer(modifier = Modifier.height(2.dp))

                    // Terceira linha (botões)
                    Row(
                        modifier = Modifier.fillMaxWidth(0.75f).padding(start = 70.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        androidx.compose.material3.Button(
                            onClick = { /* Ação do botão 1 */ },
                            modifier = Modifier
                                .width(65.dp)
                                .height(25.dp),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF919191)
                            )
                        ) {
                            androidx.compose.material3.Text(
                                text = "Limpar",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }

                        androidx.compose.material3.Button(
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
                            androidx.compose.material3.Text(
                                text = "Filtrar",
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
                    // Header da caixa grande com campo de pesquisa
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Título "Produtos"
                        androidx.compose.material3.Text(
                            text = "Produtos",
                            fontSize = 20.sp,
                            color = Color.Black
                        )

                        // Campo de pesquisa e botão
                        Row(
                            modifier = Modifier.padding(0.dp),
                            verticalAlignment = Alignment.CenterVertically // Alinha o texto e input ao centro
                        ) {
                            // Texto "Buscar:"
                            androidx.compose.material3.Text(
                                text = "Buscar:",
                                fontSize = 12.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            // Input para pesquisa com borda arredondada apenas à esquerda
                            Box(
                                modifier = Modifier
                                    .width(105.dp)
                                    .height(20.dp) // Define a altura personalizada
                                    .border(1.dp, Color.Gray, RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                                    .background(Color.White, RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                            ) {
                                BasicTextField(
                                    value = "", // Substituir pelo estado da pesquisa
                                    onValueChange = { /* Ação ao mudar o valor */ },
                                    singleLine = true,
                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                                    modifier = Modifier
                                        .fillMaxSize()
//                                        .padding(start = 8.dp, vertical = 8.dp) // Adiciona padding interno para o texto
                                )
                            }

                            // Botão com ícone e borda arredondada apenas à direita
                            androidx.compose.material3.Button(
                                onClick = { /* Ação ao clicar no botão de pesquisa */ },
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(20.dp), // Altura ajustada
                                shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp), // Arredonda apenas a direita
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF355070) // Cor do botão
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.mipmap.search), // Ícone do mipmap
                                    contentDescription = "Pesquisar",
                                    tint = Color.White, // Cor do ícone
                                    modifier = Modifier.size(16.dp) // Tamanho do ícone
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

                        ProductTable(products, { product -> }, { product -> })

                    }
                }
            }

            // Dois botões azuis na parte inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(50.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento entre os botões
            ) {
                androidx.compose.material3.Button(
                    onClick = { /* Ação do primeiro botão */ },
                    modifier = Modifier
                        .weight(1f) // Para garantir que os dois botões tenham o mesmo tamanho
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF355070)
                    )
                ) {
                    androidx.compose.material3.Text(
                        text = "Código",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                androidx.compose.material3.Button(
                    onClick = {
                         isModalNovoProd = true
                              },
                    modifier = Modifier
                        .weight(1f) // Para garantir que os dois botões tenham o mesmo tamanho
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF355070)
                    )
                ) {
                    androidx.compose.material3.Text(
                        text = "Novo Produto",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
                if (isModalNovoProd){
                    NovoProdutoDialog(
                        onDismissRequest = { isModalNovoProd = false }
                    )
                }
            }


            // Dois botões azuis na parte inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(50.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento entre os botões
            ) {
                androidx.compose.material3.Button(
                    onClick = {
                            isModalAddProd = true
                    },
                    modifier = Modifier
                        .weight(1f) // Para garantir que os dois botões tenham o mesmo tamanho
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF355070)
                    )
                ) {
                    androidx.compose.material3.Text(
                        text = "ADD Produto",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                if (isModalAddProd){
                    AddProdutoEstoque(
                        onDismissRequest = { isModalAddProd = false }
                    )
                }

                androidx.compose.material3.Button(
                    onClick = {
                         isModalNovoModelo = true
                              },
                    modifier = Modifier
                        .weight(1f) // Para garantir que os dois botões tenham o mesmo tamanho
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF355070)
                    )
                ) {
                    androidx.compose.material3.Text(
                        text = "Novo Modelo",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
                if (isModalNovoModelo){
                    ModalNovoModeloDialog(
                        onDismissRequest = { isModalNovoModelo = false }
                    )
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
