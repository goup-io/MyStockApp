import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

// componentes
import com.example.mystockapp.modais.FormField
import com.example.mystockapp.components.FormFieldCheck
import com.example.mystockapp.modais.ModalHeaderComponent
import com.example.mystockapp.modais.componentes.ButtonComponent
import com.example.mystockapp.modais.componentes.SelectField


@Composable
fun InformacoesProdutoDialog(onDismissRequest: () -> Unit) {
    var isPromocional by remember { mutableStateOf(true) }
    var codigo by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var tamanho by remember { mutableStateOf("") }
    var loja by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var cor by remember { mutableStateOf("") }
    var nItens by remember { mutableStateOf("") }

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
                ModalHeaderComponent(onDismissRequest = onDismissRequest, "Informações do Produto")
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
                                    textValue = codigo,
                                    onValueChange = { codigo = it },
                                    fieldType = KeyboardType.Number
                                )
                                SelectField(
                                    label = "Modelo:",
                                    selectedOption = modelo,
                                    options = listOf("Modelo 1", "Modelo 2", "Modelo 3"),
                                    onOptionSelected = { modelo = it }
                                )
                                FormField(
                                    label = "Tamanho:",
                                    textValue = tamanho,
                                    onValueChange = { tamanho = it }
                                )
                                FormField(
                                    label = "Loja:",
                                    textValue = loja,
                                    onValueChange = { loja = it }
                                )
                                FormFieldCheck(
                                    label = "Item Promocional",
                                    isChecked = isPromocional,
                                    onCheckedChange = { isPromocional = it }
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                FormField(
                                    label = "Nome:",
                                    textValue = nome,
                                    onValueChange = { nome = it }
                                )
                                FormField(
                                    label = "Preço:",
                                    textValue = preco,
                                    fieldType = KeyboardType.Number,
                                    onValueChange = { preco = it }
                                )
                                FormField(
                                    label = "Cor:",
                                    textValue = cor,
                                    onValueChange = { cor = it }
                                )
                                FormField(
                                    label = "N. Itens:",
                                    textValue = nItens,
                                    fieldType = KeyboardType.Number,
                                    onValueChange = { nItens = it}
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ButtonComponent(
                        titulo = "Excluir",
                        onClick = { /* Salvar action */ },
                        containerColor = Color(0xFF919191),
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    ButtonComponent(
                        titulo = "Editar",
                        onClick = onDismissRequest,
                        containerColor = Color(0xFF355070),
                    )

                }
            }
        }
    }
}

@Preview
@Composable
fun InformacoesProdutoDialogPreview() {
    // Simulando o comportamento de dismiss para visualização no preview
    InformacoesProdutoDialog(onDismissRequest = {})
}
