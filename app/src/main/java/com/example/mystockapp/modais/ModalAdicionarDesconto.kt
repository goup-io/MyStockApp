package com.example.mystockapp.modais

import DottedLineComponent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mystockapp.modais.componentes.ButtonComponent

@Composable
fun ModalAdicionarDesconto(onDismissRequest: () -> Unit) {
    var porcentagem by remember { mutableStateOf("") }
    var valorCalculado by remember { mutableStateOf("R$ 0,00") } // Valor inicial ajustado
    var valorAtual by remember { mutableStateOf("R$ 400,00") }

    // Função para calcular o valor após aplicar a porcentagem
    fun calcularValorComDesconto() {
        // Remove caracteres não numéricos e converte para Float
        val valorNumericoAtual = valorAtual.replace("[^\\d,]".toRegex(), "").replace(",", ".").toFloatOrNull() ?: 0f
        val porcentagemNumerica = porcentagem.toFloatOrNull() ?: 0f

        // Calcula o valor do desconto e o valor final
        val desconto = valorNumericoAtual * (porcentagemNumerica / 100)
        val valorFinal = valorNumericoAtual - desconto

        // Atualiza o valor calculado com a formatação correta
        valorCalculado = "R$ %.2f".format(valorFinal)
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
                ModalHeaderComponent(onDismissRequest = onDismissRequest, "Adicionar Desconto")
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
                                    label = "Porcentagem %:",
                                    textValue = porcentagem,
                                    width = 300.dp,
                                    fieldType = KeyboardType.Number,
                                    onValueChange = {
                                        porcentagem = it
                                        calcularValorComDesconto() // Atualiza o valor calculado sempre que a porcentagem muda
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
                                    label = "Valor Calculado:",
                                    textValue = valorCalculado,
                                    width = 300.dp,
                                    onValueChange = { valorCalculado = it },
                                    disabled = true
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        DottedLineComponent() // Linha pontilhada
                        Spacer(modifier = Modifier.height(4.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Linha para o valor atual
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Valor Atual",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = valorAtual,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
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
                                    text = "Valor após o desconto",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = valorPosDesconto,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            ButtonComponent(
                                titulo = "Limpar",
                                onClick = {
                                    porcentagem = ""
                                    valorCalculado = ""
                                },
                                containerColor = Color(0xFF919191),
                            )
                            ButtonComponent(
                                titulo = "Salvar",
                                onClick = {
                                    val porcentagemFloat = porcentagem.toFloatOrNull() ?: 0f
                                    val valorCalculadoFloat = valorCalculado.toFloatOrNull() ?: 0f
                                    onDismissRequest() },
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
    ModalAdicionarDesconto(onDismissRequest = {})
}
