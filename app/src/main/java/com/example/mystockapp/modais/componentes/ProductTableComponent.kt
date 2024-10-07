import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.models.produtos.ProdutoTable
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ProductTable(
    products: List<ProdutoTable>,
    onAddProduto: (ProdutoTable) -> Unit,
    onRemoverProduto: (ProdutoTable) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 250.dp)
            .border(2.dp, Color(0xFF355070), RoundedCornerShape(5.dp))
    ) {
        Column {
            TableHeader()
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(products.size) { index ->
                    val product = products[index]
                    ProductRow(
                        product = product,
                        addProdutoOpcao = onAddProduto,
                        removerProdutoOpcao = onRemoverProduto,
                        backgroundColor = if (index % 2 == 0) Color(0xFFE7E7E7) else Color(0xFFD0D4F0)
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
            .background(Color(0xFF355070), RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
            .padding(2.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeaderText("Nome", Modifier.weight(2f))
        HeaderText("Modelo", Modifier.weight(2f))
        HeaderText("Preço", Modifier.weight(1.2f))
        HeaderText("Tamanho", Modifier.weight(1.5f))
        HeaderText("Cor", Modifier.weight(1f))
        HeaderText("N. Itens", Modifier.weight(1f))
        HeaderText("Add", Modifier.weight(1.3f))
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
    addProdutoOpcao: (ProdutoTable) -> Unit,
    removerProdutoOpcao: (ProdutoTable) -> Unit,
    backgroundColor: Color
) {
    var quantidadeToAdd by remember { mutableStateOf(product.quantidadeToAdd) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .border(1.dp, Color(0xFF355070))
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
            modifier = Modifier.weight(2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                product.modelo,
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
                product.tamanho.toString(),
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                maxLines = 2, // Limitar a 2 linhas, ou ajustar conforme necessário
                overflow = TextOverflow.Clip, // Cortar o texto sem reticências
                lineHeight = 12.sp // Ajustar a altura da linha para evitar espaçamento excessivo
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                product.cor,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                maxLines = 2, // Limitar a 2 linhas, ou ajustar conforme necessário
                overflow = TextOverflow.Clip, // Cortar o texto sem reticências
                lineHeight = 12.sp // Ajustar a altura da linha para evitar espaçamento excessivo
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                product.quantidade.toString(),
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
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.weight(1.3f)
        ) {
            Button(
                onClick = {
                    removerProdutoOpcao(product)
                    quantidadeToAdd = product.quantidadeToAdd
                },
                modifier = Modifier
                    .height(24.dp)
                    .width(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("-", fontSize = 15.sp, color = Color.Black, lineHeight = 20.sp)
                }
            }

            BasicTextField(
                value = quantidadeToAdd.toString(),
                onValueChange = { },
                modifier = Modifier
                    .size(13.dp)
                    .border(1.dp, Color(0xFF355070), RoundedCornerShape(50))
                    .background(Color.Transparent),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 6.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 10.sp
                ),
                singleLine = true,
                enabled = false,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .clipToBounds()
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        innerTextField()
                    }
                }
            )

            Button(
                onClick = {
                    addProdutoOpcao(product)
                    quantidadeToAdd = product.quantidadeToAdd
                },
                modifier = Modifier
                    .height(24.dp)
                    .width(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", fontSize = 15.sp, color = Color.Black, lineHeight = 20.sp)
                }
            }
        }
    }
}