package com.example.mystockapp.telas

import InformacoesProdutoDialog
import NovoProdutoDialog
import ProductTable
import android.content.Intent
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.mystockapp.telas.componentes.ScreenTable
import com.example.mystockapp.telas.componentes.MenuDrawer
import com.example.mystockapp.telas.componentes.Spinner
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
    var showTest by remember { mutableStateOf(false) }

    val products = listOf(
        ProdutoTable(0,"166267274711114","Triple Black", "Air Force", 300.00, 37, "Preto",  20),
        ProdutoTable(0,"1662672747141111111","Classic White", "Air Max", 400.00, 38, "Branco", 15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714", "Classic White", "Air Max", 400.00, 38, "Branco", 15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco",  15),
        ProdutoTable(0,"166267274714","Classic White", "Air Max", 400.00, 38, "Branco", 15)
    )

    // contexto local (a tela atual)
    val contexto = LocalContext.current

    MenuDrawer(titulo = stringResource(id = R.string.titulo_estoque)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF355070))
        ) {
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
                        .background(Color.White, RoundedCornerShape(8.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Primeira linha (label e input)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // SelectField para Modelo
                            SelectField(
                                label = stringResource(id = R.string.label_modelo),
                                selectedOption = "",
                                options = listOf("Modelo 1", "Modelo 2", "Modelo 3"),
                                onOptionSelected = { /* Ação ao selecionar */ },
                                modifier = Modifier.weight(1.4f)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // SelectField para Cor
                            SelectField(
                                label = stringResource(id = R.string.label_cor),
                                selectedOption = "",
                                options = listOf("Cor 1", "Cor 2", "Cor 3"),
                                onOptionSelected = { /* Ação ao selecionar */ },
                                modifier = Modifier.weight(1.4f)
                            )
                        }

                        // Segunda linha (SelectField para Tam. e Preço)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // SelectField para Tamanho
                            SelectField(
                                label = stringResource(id = R.string.label_tamanho),
                                selectedOption = "",
                                options = listOf("P", "M", "G"),
                                onOptionSelected = { /* Ação ao selecionar */ },
                                modifier = Modifier.weight(1.4f)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // SelectField para Preço
                            SelectField(
                                label = stringResource(id = R.string.label_preco),
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
                                    text = stringResource(id = R.string.botao_limpar),
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }

                            androidx.compose.material3.Button(
                                onClick = { showTest = true },
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
                                    text = stringResource(id = R.string.botao_filtrar),
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }

                            if (showTest) {
                                InformacoesProdutoDialog(
                                    onDismissRequest = { showTest = false },
                                    idProduto = 1
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
                                text = stringResource(id = R.string.titulo_produtos),
                                fontSize = 20.sp,
                                color = Color.Black
                            )

                            // Campo de pesquisa e botão
                            Row(
                                modifier = Modifier.padding(0.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Texto "Buscar:"
                                androidx.compose.material3.Text(
                                    text = stringResource(id = R.string.label_buscar),
                                    fontSize = 12.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(end = 8.dp)
                                )

                                // Input para pesquisa
                                Box(
                                    modifier = Modifier
                                        .width(105.dp)
                                        .height(20.dp)
                                        .border(1.dp, Color.Gray, RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                                        .background(Color.White, RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                                ) {
                                    BasicTextField(
                                        value = "", // Substituir pelo estado da pesquisa
                                        onValueChange = { /* Ação ao mudar o valor */ },
                                        singleLine = true,
                                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                // Botão com ícone
                                androidx.compose.material3.Button(
                                    onClick = { /* Ação ao clicar no botão de pesquisa */ },
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(20.dp),
                                    shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF355070)
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.mipmap.search),
                                        contentDescription = stringResource(id = R.string.descricao_pesquisar),
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
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
                            ScreenTable(products, { product -> }, false)
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    androidx.compose.material3.Button(
                        onClick = { /* Ação do primeiro botão */
                            val telaBip = Intent(contexto, BipScreen::class.java)
                            telaBip.putExtra("contextoBusca", "estoque")
                            contexto.startActivity(telaBip)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF355070)
                        )
                    ) {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.botao_codigo),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    androidx.compose.material3.Button(
                        onClick = {
                            isModalNovoProd = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF355070)
                        )
                    ) {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.botao_novo_produto),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    if (isModalNovoProd) {
                        NovoProdutoDialog(
                            onDismissRequest = { isModalNovoProd = false }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    androidx.compose.material3.Button(
                        onClick = {
                            isModalAddProd = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF355070)
                        )
                    ) {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.botao_add_produto),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    if (isModalAddProd) {
                        AddProdutoEstoque(
                            onDismissRequest = { isModalAddProd = false }
                        )
                    }

                    androidx.compose.material3.Button(
                        onClick = {
                            isModalNovoModelo = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF355070)
                        )
                    ) {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.botao_novo_modelo),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    if (isModalNovoModelo) {
                        ModalNovoModeloDialog(
                            onDismissRequest = { isModalNovoModelo = false }
                        )
                    }
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
