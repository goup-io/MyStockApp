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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.models.produtos.ProdutoTable
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.ViewModel
import com.example.mystockapp.R
import com.example.mystockapp.modais.ModalAdicionar
import com.example.mystockapp.modais.viewModels.ProdutoViewModel

@Composable
fun ProductTable(
    products: List<ProdutoTable>,
    onAddProduto: (ProdutoTable) -> Unit,
    onRemoverProduto: (ProdutoTable) -> Unit,
    isPreVenda: Boolean = false,
    viewModel: ProdutoViewModel
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
                        removeProdutoOpcao = onRemoverProduto,
                        backgroundColor = if (index % 2 == 0) Color(0xFFE7E7E7) else Color(0xFFD0D4F0),
                        isPreVenda = isPreVenda,
                        viewModel = viewModel
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
        HeaderText("Preço", Modifier.weight(1.2f))
        HeaderText("Tamanho", Modifier.weight(1.5f))
        HeaderText("Cor", Modifier.weight(1f))
        HeaderText("Add", Modifier.weight(1.2f))
    }
}

@Composable
fun HeaderText(text: String, modifier: Modifier) {
    Text(
        text,
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = Color.White,
        fontSize = 12.sp,
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
    backgroundColor: Color,
    isPreVenda: Boolean,
    viewModel: ProdutoViewModel,
    removeProdutoOpcao: (ProdutoTable) -> Unit
) {
    var showModalAdd by remember { mutableStateOf(false) }

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
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Clip,
                lineHeight = 14.sp
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
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Clip,
                lineHeight = 14.sp
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
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Clip,
                lineHeight = 14.sp
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
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Clip,
                lineHeight = 12.sp
            )
        }
        Column (
            modifier = Modifier.weight(1.2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    // Ação a ser realizada quando o botão é clicado
                    // TODO: Utilizar o viewModel para adicionar o produto e assim mudar o estado do botão
                    viewModel.escolherProduto(product)
//                    showModalAdd = true
                },
            ) {
                Icon(
                    painter = painterResource(id = R.mipmap.vermais),
                    contentDescription = "Adicionar",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
//        if (isPreVenda && showModalAdd) {
//            ModalAdicionar(
//                onDismissRequest = { showModalAdd = false },
//                produto = product,
////                onAddProduto = { addProdutoOpcao(product) },
////                onRemoveProduto = { removeProdutoOpcao(product) },
//                viewModel = viewModel
//            )
//        }
//        if(!isPreVenda && showModalAdd) {
////            ModalAdicionar(
////                onDismissRequest = { showModalAdd = false },
////                idProduto = product.id,
////                onAddProduto = { addProdutoOpcao(product) },
////                onRemoveProduto = { removeProdutoOpcao(product) },
////                viewModel = viewModel
////            )
//
//            //todo: modal de add no estoque
//        }
    }
}