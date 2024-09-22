package com.example.mystockapp.modais

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mystockapp.modais.componentes.ButtonComponent

@Composable
fun ConfirmacaoDialog(
    titulo: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmarBtnTitulo: String,
    recusarBtnTitulo: String = "Cancelar",
    imagem: Painter,
    btnConfirmColor: Color = Color(0xFF355070),
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(15.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = imagem,
                    contentDescription = "Ícone de Ação",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    titulo,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                     ButtonComponent(
                            titulo = confirmarBtnTitulo,
                            onClick = onConfirm,
                            containerColor = btnConfirmColor,
                     )
                    Spacer(modifier = Modifier.width(16.dp))
                    ButtonComponent(
                        titulo = recusarBtnTitulo,
                        onClick = onDismiss,
                        containerColor = Color(0xFF919191),
                    )
                }
            }
        }
    }
}