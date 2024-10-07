package com.example.mystockapp.telas.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.mystockapp.R
import com.example.mystockapp.models.produtos.ProdutoTable

@Composable
fun ScreenTable(
    products: List<ProdutoTable>,
    verMaisAction: (ProdutoTable) -> Unit,
    isPreVenda: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFF355070))
    ) {
        Column (
            modifier = Modifier
                .fillMaxHeight()
                .background(Color(0xFF355070))
        ){
            TableHeader()
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color(0xFF355070))
            ) {
                items(products.size) { index ->
                    val product = products[index]
                    ProductRow(
                        product = product,
                        verMaisAction = verMaisAction,
                        backgroundColor = if (index % 2 == 0) Color(0xFFE7E7E7) else Color(0xFFD0D4F0),
                        isPreVenda = isPreVenda
                    )
                }
            }
        }
    }
}

@Composable
fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF355070), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            .padding(2.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeaderText("Codigo", Modifier.weight(2f))
        HeaderText("Nome", Modifier.weight(2f))
        HeaderText("Preço", Modifier.weight(1.2f))
        HeaderText("Q. Itens", Modifier.weight(1.5f))
        HeaderText("Ver Mais", Modifier.weight(1.3f))
    }
}


@Composable
fun HeaderText(text: String, modifier: Modifier) {
    Text(
        text,
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = Color.White,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRow(
    product: ProdutoTable,
    verMaisAction: (ProdutoTable) -> Unit,
    backgroundColor: Color,
    isPreVenda: Boolean
) {

    val quantidade = if (isPreVenda) product.quantidadeToAdd else product.quantidade
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, Color(0xFF355070), RoundedCornerShape(12.dp)) // Mantém a borda
            .padding(4.dp, 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                product.codigo,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                maxLines = 2, // Limitar a 2 linhas, ou ajustar conforme necessário
                overflow = TextOverflow.Clip, // Cortar o texto sem reticências
                lineHeight = 12.sp // Ajustar a altura da linha para evitar espaçamento excessivo
            )
        }
        Column(
            modifier = Modifier.weight(2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                product.nome,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                maxLines = 2, // Limitar a 2 linhas, ou ajustar conforme necessário
                overflow = TextOverflow.Clip, // Cortar o texto sem reticências
                lineHeight = 12.sp // Ajustar a altura da linha para evitar espaçamento excessivo
            )
        }
        Column(
            modifier = Modifier.weight(1.2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                product.preco.toString(),
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                maxLines = 2, // Limitar a 2 linhas, ou ajustar conforme necessário
                overflow = TextOverflow.Clip, // Cortar o texto sem reticências
                lineHeight = 12.sp // Ajustar a altura da linha para evitar espaçamento excessivo
            )
        }
        Column(
            modifier = Modifier.weight(1.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                 quantidade.toString(),
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                maxLines = 2, // Limitar a 2 linhas, ou ajustar conforme necessário
                overflow = TextOverflow.Clip, // Cortar o texto sem reticências
                lineHeight = 12.sp // Ajustar a altura da linha para evitar espaçamento excessivo
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1.3f)
        ) {
            IconButton(
                onClick = {
                    // Ação a ser realizada quando o botão é clicado
                    verMaisAction(product)
                },
            ) {
                Icon(
                    painter = painterResource(id = R.mipmap.vermais), // Substitua pelo ícone desejado
                    contentDescription = "Adicionar", // Descrição acessível do ícone
                    modifier = Modifier.size(20.dp) // Define o tamanho do ícone
                )
            }
        }

    }
}