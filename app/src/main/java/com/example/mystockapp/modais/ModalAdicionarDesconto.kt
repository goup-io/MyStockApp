package com.example.mystockapp.modais

import DottedLineComponent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mystockapp.R
import com.example.mystockapp.modais.componentes.ButtonComponent
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.models.vendas.VendaDetalhes

@Composable
fun ModalAdicionarDesconto(
    vendaDetalhes: VendaDetalhes = VendaDetalhes(0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
    produtoInfo: ProdutoTable = ProdutoTable(0, "", "", "", 0.0, 0, "", 0, 0, 0.0),
    isDescontoProduto: Boolean,
    onSalvarDescontoVenda: (Double, Double) -> Unit,
    onSalvarDescontoProduto:(Double) -> Unit,
    fontSize: TextUnit = 12.sp,
    onDismissRequest: () -> Unit
) {

    // Declare as variáveis fora do bloco if-else
    var porcentagem by remember { mutableStateOf("") }
    var valorCalculado by remember { mutableStateOf("R$ 0,00") } // Valor inicial ajustado
    var valorAtual by remember { mutableStateOf("R$ 0,00") }

    // Inicialize as variáveis de acordo com a condição
    if (!isDescontoProduto) {
        porcentagem = vendaDetalhes.porcentagemDesconto.toString()
        valorAtual = "R$ %.2f".format(vendaDetalhes.subtotal2)
        valorCalculado = "R$ %.2f".format(vendaDetalhes.valorDescontoVenda)
    } else {
        porcentagem = (((produtoInfo.valorDesconto * produtoInfo.quantidadeToAdd) / (produtoInfo.preco * produtoInfo.quantidadeToAdd)) * 100.0f).toString()
        valorAtual = "R$ %.2f".format((produtoInfo.preco * produtoInfo.quantidadeToAdd))
        valorCalculado = "R$ %.2f".format(produtoInfo.valorDesconto * produtoInfo.quantidadeToAdd)
    }

    // Função para calcular o valor após aplicar a porcentagem
    fun calcularValorComDesconto() {
        // Remove caracteres não numéricos e converte para Float
        val valorNumericoAtual = valorAtual
            .replace("[^\\d,.]".toRegex(), "") // Mantém números, pontos e vírgulas
            .replace(",", ".") // Substitui vírgula por ponto
            .toFloatOrNull() ?: 0f

        // Converte a porcentagem para float, usando ponto como separador
        val porcentagemNumerica = porcentagem.replace(",", ".").toFloatOrNull() ?: 0f

        // Calcula o valor do desconto e o valor final
        val desconto = valorNumericoAtual * (porcentagemNumerica / 100)

        // Atualiza o valor calculado com a formatação correta
        valorCalculado = "R$ %.2f".format(desconto)
    }

    // Atualiza o valor calculado sempre que a porcentagem muda
    val valorPosDesconto by remember {
        derivedStateOf {
            val valorAtualFloat = valorAtual.replace("[^\\d,]".toRegex(), "").replace(",", ".").toFloatOrNull() ?: 0f
            val valorCalculadoFloat = valorCalculado.replace("[^\\d,]".toRegex(), "").replace(",", ".").toFloatOrNull() ?: 0f
            val resultado = valorAtualFloat - valorCalculadoFloat
            "R$ %.2f".format(resultado)
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
                ModalHeaderComponent(onDismissRequest = onDismissRequest, stringResource(id = R.string.adicionar_desconto_title))
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE7E7E7))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        // Campo para a porcentagem
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                FormField(
                                    label = stringResource(id = R.string.porcentagem_label),
                                    placeholder = "0.0",
                                    textValue = if (porcentagem == "0.0" || porcentagem.isNullOrBlank() || porcentagem == "NaN" || porcentagem == "00,00" || porcentagem == "0,00" || porcentagem == "0.00" || porcentagem.toFloat() <= 0.0) "" else "%.2f".format(porcentagem.toFloat()),
                                    width = 300.dp,
                                    fieldType = KeyboardType.Number,
                                    onValueChange = { input ->
                                        val formattedInput = input.replace(",", ".")

                                        val porcentagemNumerica = formattedInput.toFloatOrNull()

                                        if (porcentagemNumerica != null && porcentagemNumerica <= 100) {
                                            porcentagem = formattedInput
                                            calcularValorComDesconto() // Atualiza o valor calculado sempre que a porcentagem muda
                                        } else if (porcentagemNumerica != null && porcentagemNumerica > 100) {
                                            porcentagem = "100"
                                            calcularValorComDesconto()
                                        }
                                    },
                                )
                            }
                        }

                        // Campo para o valor calculado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                FormField(
                                    label = stringResource(id = R.string.valor_calculado_label),
                                    textValue = valorCalculado,
                                    width = 300.dp,
                                    onValueChange = { valorCalculado = it },
                                    disabled = true
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if(isDescontoProduto){
                                // Linha para o valor unitário produto
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.valor_unitario_label),
                                        fontSize = fontSize,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Text(
                                        text = "R$ %.2f".format(produtoInfo.preco),
                                        fontSize = fontSize,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                DottedLineComponent() // Linha pontilhada
                                Spacer(modifier = Modifier.height(3.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.valor_unitario_com_desconto),
                                        fontSize = fontSize,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Text(
                                        text = if (porcentagem == "0.0" || porcentagem.isNullOrBlank() || porcentagem == "NaN") "R$ %.2f".format(produtoInfo.preco) else "R$ %.2f".format((produtoInfo.preco * (1 - (porcentagem.toFloat() / 100)))),
                                        fontSize = fontSize,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                DottedLineComponent() // Linha pontilhada
                                Spacer(modifier = Modifier.height(3.dp))
                            }
                            // Linha para o valor atual
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(id = R.string.valor_atual_label),
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Normal
                                )
                                Text(
                                    text = valorAtual,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            DottedLineComponent()
                            Spacer(modifier = Modifier.height(3.dp))

                            // Linha para o valor pós-desconto
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(id = R.string.valor_apos_desconto_label),
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = valorPosDesconto,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            ButtonComponent(
                                titulo = stringResource(id = R.string.limpar_button),
                                onClick = {
                                    valorCalculado = "R$ 0,00"
                                    porcentagem = ""
                                },
                                containerColor = Color(0xFF919191),
                            )
                            ButtonComponent(
                                titulo = stringResource(id = R.string.salvar_button),
                                onClick = {
                                    if (isDescontoProduto){
                                       val valorUnitDesconto = produtoInfo.preco - (produtoInfo.preco * (1 - (porcentagem.toFloat() / 100)))
                                        onSalvarDescontoProduto(valorUnitDesconto)
                                    } else {
                                        val valorCalculadoDouble = valorCalculado
                                            .replace("[^\\d,.]".toRegex(), "")
                                            .replace(",", ".")
                                            .toDoubleOrNull() ?: 0.0
                                        onSalvarDescontoVenda(valorCalculadoDouble, porcentagem.toDouble())
                                    }
                                    onDismissRequest()
                                },
                                containerColor = Color(0xFF355070),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.width(18.dp))
                }
            }
        }
    }
}


@Preview
@Composable
fun ModalAdicionarDescontoPreview() {
    // Simulando o comportamento de dismiss para visualização no preview
    ModalAdicionarDesconto(
        vendaDetalhes = VendaDetalhes(0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
        isDescontoProduto = false,
        onSalvarDescontoVenda = { _, _ -> },
        onDismissRequest = {},
        onSalvarDescontoProduto = { _ -> }
    )
}
