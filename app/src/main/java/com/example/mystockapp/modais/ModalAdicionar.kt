package com.example.mystockapp.modais
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.mystockapp.components.FormFieldCheck
import com.example.mystockapp.modais.componentes.SelectField
import com.example.mystockapp.modais.componentes.utils.desformatarPreco
import com.example.mystockapp.modais.componentes.utils.formatarPreco
import com.example.mystockapp.modais.viewModels.ProdutoViewModel
import com.example.mystockapp.models.produtos.Cor
import com.example.mystockapp.models.produtos.ItemPromocional
import com.example.mystockapp.models.produtos.Modelo
import com.example.mystockapp.models.produtos.Produto
import com.example.mystockapp.models.produtos.Tamanho
import com.example.mystockapp.telas.viewModels.PreVendaViewModel

@Composable
fun ModalAdicionar(
    onDismissRequest: () -> Unit,
    onConfirmarAdd: (quantidade:Int) -> Unit,
    viewModel: ProdutoViewModel,
    isPreVenda: Boolean
) {

    var isPromocional by remember { mutableStateOf(false) }

    var modelosOptions by remember { mutableStateOf<List<Modelo>>(emptyList()) }
    var coresOptions by remember { mutableStateOf<List<Cor>>(emptyList()) }
    var tamanhosOptions by remember { mutableStateOf<List<Tamanho>>(emptyList()) }

    Log.d("ModalAdicionar", "Produto Selecionado: ${viewModel.produtoSelecionado}")
    var quantidadeAdd by remember { mutableStateOf(0) }

    var showError by remember { mutableStateOf(false) }

    var tempPreco by remember { mutableStateOf("") }

    var produtoInfo by remember { mutableStateOf(Produto(
        0,
        "",
        "",
        "",
        0.0,
        "",
        0,
        0,
        "",
        ItemPromocional.NAO,
        0
    )) }

    LaunchedEffect(Unit) {
        val produtoSelecionadoId = viewModel.produtoSelecionado?.id
        if (produtoSelecionadoId != null) {
            val produtoSelecionado = viewModel.pesquisarProdutoPorId(produtoSelecionadoId)
            if (produtoSelecionado != null) {
                produtoInfo = produtoSelecionado
                quantidadeAdd = viewModel.produtoSelecionado?.quantidadeToAdd ?: 0
                tempPreco = formatarPreco(produtoInfo.preco.toString().replace(".", ","))
                Log.d("ModalAdicionar", "Produto selecionado: ${viewModel.produtoSelecionado}")
                Log.d("ModalAdicionar", "quantidade que já tem: ${viewModel.produtoSelecionado?.quantidadeToAdd}")
            } else {
                Log.d("ModalAdicionar", "Produto não encontrado.")
            }
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(15.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                ModalHeaderComponent(onDismissRequest = onDismissRequest, if (isPreVenda) "Adicionar no Carrinho" else "Adicionar no Estoque")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE7E7E7))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                FormField(
                                    label = "Codigo:",
                                    textValue = produtoInfo.codigo,
                                    onValueChange = { produtoInfo = produtoInfo.copy(codigo = it) },
                                    fieldType = KeyboardType.Number,
                                    disabled = true,
                                    error = showError && produtoInfo.codigo.isEmpty()
                                )
                                SelectField(
                                    label = "Modelo:",
                                    selectedOption = produtoInfo.modelo,
                                    options = modelosOptions,
                                    disabled = true,
                                    onOptionSelected = { produtoInfo = produtoInfo.copy(modelo = it) },
                                    error = showError && produtoInfo.modelo.isEmpty()
                                )
                                SelectField(
                                    label = "Tamanho:",
                                    selectedOption = produtoInfo.tamanho.toString(),
                                    disabled = true,
                                    options = tamanhosOptions,
                                    onOptionSelected = { produtoInfo = produtoInfo.copy(tamanho = it.toInt()) },
                                )
                                SelectField(
                                    label = "Cor:",
                                    selectedOption = produtoInfo.cor,
                                    disabled = true,
                                    options = coresOptions,
                                    onOptionSelected = { produtoInfo = produtoInfo.copy(cor = it) }
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                FormField(
                                    label = "Nome:",
                                    textValue = produtoInfo.nome,
                                    disabled = true,
                                    onValueChange = { produtoInfo = produtoInfo.copy(nome = it) },
                                    error = showError && produtoInfo.nome.isEmpty()
                                )
                                FormField(
                                    label = "Preço:",
                                    textValue = tempPreco,
                                    fieldType = KeyboardType.Decimal,
                                    onValueChange = { input ->
                                        val precoFormatado = formatarPreco(input)
                                        tempPreco = precoFormatado
                                        produtoInfo = produtoInfo.copy(preco = desformatarPreco(precoFormatado))
                                        Log.d("InformacoesProdutoDialog", "Preço Custo: ${produtoInfo.preco}")
                                    },
                                    disabled = true,
                                    error = showError && produtoInfo.preco <= 0.0
                                )
                                FormField(
                                    label = "N. Itens:",
                                    textValue = produtoInfo.quantidade.toString(),
                                    fieldType = KeyboardType.Number,
                                    onValueChange = { newValue ->
                                        val regex = "^[0-9]*$".toRegex()
                                        if (newValue.matches(regex)) {
                                            produtoInfo = produtoInfo.copy(quantidade = newValue.toIntOrNull() ?: 0)
                                        }
                                    },
                                    disabled = true,
                                    error = showError && produtoInfo.quantidade < 0
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                FormFieldCheck(
                                    label = "Item Promocional",
                                    isChecked = produtoInfo.itemPromocional == ItemPromocional.SIM,
                                    disabled = true,
                                    onCheckedChange = {
                                        isPromocional = it
                                        if (isPromocional) {
                                            produtoInfo.copy(itemPromocional = ItemPromocional.SIM)
                                        } else {
                                            produtoInfo.copy(itemPromocional = ItemPromocional.NAO)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if(isPreVenda){
                        Column(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .weight(1f),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = "R$ ${formatarPreco((produtoInfo.preco * quantidadeAdd).toString().replace(".", ","))}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Left,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(0.dp),
                                color = Color.Black
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .weight(1f),
                            verticalArrangement = Arrangement.Bottom
                        ) { }
                    }
                    // Segunda Coluna (Box com Row)
                    Column(
                        modifier = Modifier
                            .background(Color.Transparent)
                            .weight(2.1f)
                            .padding(horizontal = 8.dp)
                            .height(45.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .padding(0.dp)
                                .padding(bottom = 8.dp)
                                .shadow(
                                    0.dp,
                                    RoundedCornerShape(10.dp),
                                    clip = false
                                )
                                .border(1.dp, Color(0xFF355070), RoundedCornerShape(5.dp))
                                .background(Color.Transparent)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        quantidadeAdd = if (quantidadeAdd > 0) quantidadeAdd - 1 else 0
                                              },
                                    modifier = Modifier
                                        .background(Color.Transparent)
                                        .padding(4.dp)
                                        .weight(0.5f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Diminuir Quantidade",
                                        tint = Color(0xFF355070)
                                    )
                                }

                                Text(
                                    text = quantidadeAdd.toString(),
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 20.sp,
                                    lineHeight = 20.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color(0xFF355070),
                                    modifier = Modifier
                                        .background(Color.Transparent)
                                        .padding(0.dp)
                                        .weight(0.8f)
                                        .align(Alignment.CenterVertically)
                                )

                                IconButton(
                                    onClick = {
                                        if(quantidadeAdd < produtoInfo.quantidade){
                                            quantidadeAdd += 1
                                        }
                                              },
                                    modifier = Modifier
                                        .background(Color.Transparent)
                                        .padding(4.dp)
                                        .weight(0.5f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Aumentar Quantidade",
                                        tint = Color(0xFF355070)
                                    )
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        horizontalAlignment = Alignment.End, 
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                onConfirmarAdd(quantidadeAdd)
                                onDismissRequest()
                            },
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .size(40.dp)
                                .shadow(15.dp, RoundedCornerShape(10.dp), clip = false)
                                .background(Color(0xFF355070), RoundedCornerShape(10.dp))
                        ) {
                            Icon(
                                imageVector = if(isPreVenda) Icons.Default.AddShoppingCart else Icons.Default.Inventory2,
                                contentDescription = "Confirmar Adição do Produto",
                                tint = Color.White
                            )
                        }
                    }
                }


            }
        }
    }



}

@Preview
@Composable
private fun PreviewModalAddProdCarrinho() {
    ModalAdicionar(
        {},
        {_, -> },
        PreVendaViewModel(1),
        true
    )
}