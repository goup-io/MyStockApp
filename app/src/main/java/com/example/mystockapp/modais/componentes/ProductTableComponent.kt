import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.dtos.ProdutoTable
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
        HeaderText("Preço", Modifier.weight(1f))
        HeaderText("Tamanho", Modifier.weight(1.5f)) // Aumentar o peso para dar mais espaço
        HeaderText("Cor", Modifier.weight(1f))
        HeaderText("N. Itens", Modifier.weight(1f))
        HeaderText("Add", Modifier.weight(1f))
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
            .padding(2.dp, 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(product.name, modifier = Modifier.weight(2f), textAlign = TextAlign.Center, fontSize = 7.sp)
        Text(product.model, modifier = Modifier.weight(2f), textAlign = TextAlign.Center, fontSize = 7.sp)
        Text(product.price, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 7.sp)
        Text(product.size, modifier = Modifier.weight(1.5f), textAlign = TextAlign.Center, fontSize = 7.sp)
        Text(product.color, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 7.sp)
        Text(product.quantity, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 7.sp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1.1f)
        ) {
            Button(
                onClick = { /* TODO: Handle button click */ },
                modifier = Modifier.width(10.dp).height(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("-", fontSize = 7.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.width(0.8.dp))
            TextField(
                value = "5",
                onValueChange = { /* TODO: Handle value change */ },
                modifier = Modifier
                    .size(width = 11.5.dp, height = 11.5.dp)
                    .padding(0.dp)
                    .border(1.dp, Color(0xFF355070), RoundedCornerShape(50)),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 7.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(0.8.dp))
            Button(
                onClick = { /* TODO: Handle button click */ },
                modifier = Modifier.width(10.dp).height(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("+", fontSize = 7.sp, color = Color.Black)
            }
        }
    }
}