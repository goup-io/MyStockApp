package com.example.mystockapp.modais

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun ModalDigitarBarcodeDialog(onDismissRequest: () -> Unit) {
    var codigo by remember { mutableStateOf("") }

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
                ModalHeaderComponent(onDismissRequest = onDismissRequest, "Digitar Codigo de Barras")
                Spacer(modifier = Modifier.height(6.dp))
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
                                    fieldSize = 30.dp
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
                                "Salvar",
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

@Preview
@Composable
fun ModalDigitarBarcodeDialogPreview() {
    // Simulando o comportamento de dismiss para visualização no preview
    ModalDigitarBarcodeDialog(onDismissRequest = {})
}
