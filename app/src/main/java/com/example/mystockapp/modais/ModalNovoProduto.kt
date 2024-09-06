import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun NewProductDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Novo Produto",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF233C)),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.size(25.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(15.dp),
                    ) {
                        Text("X", color = Color.White, fontSize = 15.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

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
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                FormField(label = "Código:")
                                FormField(label = "Modelo:")
                                FormField(label = "Tamanho:")
                                FormField(label = "Loja:")
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                FormField(label = "Nome:")
                                FormField(label = "Preço:")
                                FormField(label = "Cor:")
                                FormField(label = "N. Itens:")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { /* Edit action */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF919191)),                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(15.dp),
                        modifier = Modifier
                            .width(80.dp) // Ajuste conforme necessário
                            .height(25.dp) // Ajuste conforme necessário
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                "Excluir",
                                color = Color.White,
                                fontSize = 11.sp, // Ajuste conforme necessário
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .wrapContentSize() // Ajusta o tamanho do texto para caber no botão
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(18.dp))
                    Button(
                        onClick = { /* Edit action */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF355070)),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(15.dp),
                        modifier = Modifier
                            .width(80.dp) // Ajuste conforme necessário
                            .height(25.dp) // Ajuste conforme necessário
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                "Editar",
                                color = Color.White,
                                fontSize = 11.sp, // Ajuste conforme necessário
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .wrapContentSize() // Ajusta o tamanho do texto para caber no botão
                            )
                        }
                    }


                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormField(label: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp, // Tamanho da fonte do rótulo
            fontWeight = FontWeight.Normal
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF355070), RoundedCornerShape(8.dp))
                .background(Color.White, RoundedCornerShape(8.dp))
        ) {
            TextField(
                value = "TESTE",
                onValueChange = { /* Update value */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp) // Ajusta a altura do TextField
                    .padding(horizontal = 8.dp), // Padding interno do TextField para ajustar o texto
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 10.sp // Tamanho da fonte do texto inserido
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent, // Fundo do TextField
                    focusedTextColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp), // Define as bordas arredondadas
                singleLine = true // Garante que o campo seja de uma linha
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}





@Preview
@Composable
fun NewProductDialogPreview() {
    // Simulando o comportamento de dismiss para visualização no preview
    NewProductDialog(onDismissRequest = {})
}
