package com.example.mystockapp.telas

import ProductTable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.mystockapp.dtos.ProdutoTable
import com.example.mystockapp.models.Produto
import com.example.mystockapp.telas.componentes.Header
import com.example.mystockapp.telas.componentes.Spinner
import com.example.mystockapp.telas.componentes.Table
import com.example.mystockapp.telas.ui.theme.MyStockAppTheme
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
                        .padding(16.dp)
                        .padding(top = 16.dp)
                ) {
                    // Primeira linha (label e input)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Label 1
                        Text(text = "Modelo:", modifier = Modifier.weight(1f))

                        // Input 1
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.weight(1f).height(10.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp)) // Espaço entre os grupos

                        // Label 2
                        Text(text = "Cor:", modifier = Modifier.weight(1f))

                        // Input 2
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.weight(1f).height(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Segunda linha (label e input)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Label 3
                        Text(text = "Tamanho:", modifier = Modifier.weight(1f))

                        // Input 3
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.weight(1f).height(10.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Label 4
                        Text(text = "Preço:", modifier = Modifier.weight(1f))

                        // Input 4
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.weight(1f).height(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

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
                                containerColor = Color(0xFF355070)
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
                    // Header da caixa grande
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.Text(
                            text = "Produtos",
                            fontSize = 20.sp,
                            color = Color.Black
                        )

                        Row {
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
                                    text = "Código",
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            androidx.compose.material3.Button(
                                onClick = { /* Ação do botão 2 */ },
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(25.dp),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF355070)
                                )
                            ) {
                                androidx.compose.material3.Text(
                                    text = "Add Prod",
                                    color = Color.White,
                                    fontSize = 12.sp
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

                        ProductTable(products)

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
                    onClick = { /* Ação do segundo botão */ },
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
                        text = "ADD Produto",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                androidx.compose.material3.Button(
                    onClick = { /* Ação do segundo botão */ },
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
