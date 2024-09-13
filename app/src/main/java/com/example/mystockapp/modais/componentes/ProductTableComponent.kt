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
import com.example.mystockapp.models.ProdutoTable
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ProductTable(products: List<ProdutoTable>) {
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
        HeaderText("Preço", Modifier.weight(1.2f)) // Ajustar o peso para dar mais espaço
        HeaderText("Tamanho", Modifier.weight(1.5f))
        HeaderText("Cor", Modifier.weight(1f))
        HeaderText("N. Itens", Modifier.weight(1f))
        HeaderText("Add", Modifier.weight(1.3f)) // Ajustar o peso para dar mais espaço
    }
}

@Composable
fun HeaderText(text: String, modifier: Modifier) {
    Text(
        text,
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = Color.White,
        fontSize = 7.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRow(product: ProdutoTable, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .border(1.dp, Color(0xFF355070))
            .padding(4.dp, 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            product.nome,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center,
            fontSize = 7.sp
        )
        Text(
            product.modelo,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center,
            fontSize = 7.sp
        )
        Text(
            product.preco.toString(),
            modifier = Modifier.weight(1.2f),
            textAlign = TextAlign.Center,
            fontSize = 7.sp
        )
        Text(
            product.tamanho.toString(),
            modifier = Modifier.weight(1.5f),
            textAlign = TextAlign.Center,
            fontSize = 7.sp
        )
        Text(
            product.cor,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 7.sp
        )
        Text(
            product.quantidade,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 7.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(1.5.dp),
            modifier = Modifier.weight(1.3f)
        ) {
            Button(
                onClick = { /* TODO: Handle button click */ },
                modifier = Modifier
                    .size(12.dp), // Removed align modifier
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
                    Text("-", fontSize = 7.sp, color = Color.Black, lineHeight = 16.sp)
                }
            }
            BasicTextField(
                value = "100",
                onValueChange = { /* TODO: Handle value change */ },
                modifier = Modifier
                    .size(16.dp)
                    .height(12.dp)
                    .border(1.dp, Color(0xFF355070), RoundedCornerShape(50))
                    .background(Color.Transparent),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 8.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                ),
                singleLine = true,
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
                onClick = { /* TODO: Handle button click */ },
                modifier = Modifier
                    .size(12.dp),
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
                    Text("+", fontSize = 8.sp, color = Color.Black, lineHeight = 16.sp)
                }
            }
            Spacer(modifier = Modifier.width(4.dp))

        }
    }
}