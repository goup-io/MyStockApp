package com.example.mystockapp.modais

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

// componentes
import com.example.mystockapp.modais.FormField
import com.example.mystockapp.components.FormFieldCheck
import com.example.mystockapp.modais.ModalHeaderComponent
import com.example.mystockapp.modais.componentes.ButtonComponent


@Composable
fun ModalNovoModeloDialog(onDismissRequest: () -> Unit) {
    var codigo by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

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
                ModalHeaderComponent(onDismissRequest = onDismissRequest, "Novo Modelo")
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
                                    onValueChange = { codigo = it }
                                )
                                FormField(
                                    label = "Categoria:",
                                    textValue = categoria,
                                    onValueChange = { categoria = it }
                                )

                            }
                            Column(modifier = Modifier.weight(1f)) {
                                FormField(
                                    label = "Nome:",
                                    textValue = nome,
                                    onValueChange = { nome = it }
                                )
                                FormField(
                                    label = "Tipo:",
                                    textValue = tipo,
                                    onValueChange = { tipo = it }
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
fun ModalNovoModeloDialogPreview() {
    // Simulando o comportamento de dismiss para visualização no preview
    ModalNovoModeloDialog(onDismissRequest = {})
}
