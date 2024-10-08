package com.example.mystockapp.telas

import ProductTable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonColors
//import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
//import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.ui.theme.MyStockAppTheme
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
import com.example.mystockapp.R
import com.example.mystockapp.dtos.ProdutoTable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStockAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
                ) {
                    PreVendaScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreVendaScreen() {

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
            Button(
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
            Text(
                text = "Pré Venda",
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
                    .background(Color.White, RoundedCornerShape(8.dp)) // Cor de fundo da caixa
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
                        Text(text = "Resumo da Venda", fontSize = 16.sp)

                        Row {
                            // Botões com bordas arredondadas
                            Button(
                                onClick = { /* Ação do botão 1 */ },
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .width(65.dp)
                                    .height(25.dp),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(0.dp) // Ajusta o padding
                            ) {
                                Text(text = "AddDisc", fontSize = 12.sp)
                            }

                            Button(
                                onClick = { /* Ação do botão 2 */ },
                                modifier = Modifier
                                    .width(25.dp)
                                    .height(25.dp),
                                shape = RoundedCornerShape(5.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF96BDCE) // Definindo a cor de fundo
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(text = "+", fontSize = 22.sp, color = Color.White) // Define a cor do texto
                            }

                        }
                    }

                    // Conteúdo da Caixa
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Código da Venda
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Código da Venda", fontSize = 14.sp)
                            Text(text = "001", fontSize = 14.sp)
                        }

                        Divider()

                        // Valor da Venda
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Valor da Venda", fontSize = 14.sp)
                            Text(text = "R$ 100,00", fontSize = 14.sp)
                        }

                        Divider()

                        // Quantidade de Itens
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Quantidade de Itens", fontSize = 14.sp)
                            Text(text = "5", fontSize = 14.sp)
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
                        Text(text = "Carrinho", fontSize = 20.sp, color = Color.Black)

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
                                Text(text = "Código", color = Color.White, fontSize = 12.sp)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
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
                                Text(text = "Add Prod", color = Color.White, fontSize = 12.sp)
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Código", fontSize = 14.sp)

                    OutlinedTextField(
                        modifier = Modifier
                            .width(80.dp)
                            .height(10.dp),
                        value = codigo,
                        onValueChange = { codigo = it }
                    )

                    Text(text = "Tipo Venda", fontSize = 14.sp)

                    OutlinedTextField(
                        modifier = Modifier.width(80.dp),
                        value = codigo,
                        onValueChange = { codigo = it }
                    )
                }
            }

            // Botão azul na parte inferior
            Button(
                onClick = { /* Ação do botão */ },
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF355070)
                )
            ) {
                Text(text = "Finalizar Venda", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

data class Item(
    val codigo: String,
    val nome: String,
    val preco: String,
    val quantidade: String,
    val verMaisResId: Int // Referência ao recurso da imagem
)



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyStockAppTheme {
        PreVendaScreen()
    }
}