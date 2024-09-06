import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun NovoProdutoDialog(onDismissRequest: () -> Unit) {
    var isPromocional by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(15.dp),

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
                        elevation = ButtonDefaults.elevatedButtonElevation(15.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                "X",
                                color = Color.White,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
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
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                FormField(label = "Código:")
                                FormField(label = "Modelo:")
                                FormField(label = "Tamanho:")
                                FormField(label = "Loja:")
                                FormFieldCheck(label = "Item Promocional")
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
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF919191)),
                        shape = RoundedCornerShape(5.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(15.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .width(80.dp)
                            .height(25.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                "Excluir",
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                              )
                        }
                    }
                    Spacer(modifier = Modifier.width(18.dp))
                    Button(
                        onClick = { /* Edit action */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF355070)),
                        shape = RoundedCornerShape(5.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(15.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .width(80.dp)
                            .height(25.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                "Editar",
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .wrapContentSize()
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
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF355070), RoundedCornerShape(5.dp))
                .background(Color.White, RoundedCornerShape(5.dp))
        ) {
            TextField(
                value = "TESTE",
                onValueChange = { /* Update value */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(horizontal = 8.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 10.sp
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormFieldCheck(label: String) {
    var isChecked by remember { mutableStateOf(true) } // Começa como true
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .clipToBounds(),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(25.dp)
                .fillMaxWidth()
                .padding(start = 0.dp, end = 0.dp)
                .clipToBounds()
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = !isChecked }, //,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF355070),
                    uncheckedColor = Color(Color.White.value)
                ),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp)
                    .clipToBounds()
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(0.dp)
                    .width(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))
    }
}





@Preview
@Composable
fun NovoProdutoDialogPreview() {
    // Simulando o comportamento de dismiss para visualização no preview
    NovoProdutoDialog(onDismissRequest = {})
}
