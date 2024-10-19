package com.example.mystockapp.modais

import DottedLineComponent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mystockapp.R
import com.example.mystockapp.models.vendas.VendaDetalhes

@Composable
fun ModalResumoVenda(onDismissRequest: () -> Unit, detalhes: VendaDetalhes) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(15.dp),
        ) {
            Column (
                modifier = Modifier.padding(16.dp)
            ) {
                ModalHeaderComponent(onDismissRequest, stringResource(id = R.string.modal_resumo_venda_title))
                ResumoLista(detalhes)
            }
        }
    }
}

@Composable
fun ResumoLista(detalhes: VendaDetalhes) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ResumoItem(stringResource(id = R.string.total_de_itens), detalhes.totalItens.toString())
        DottedLineComponent()
        ResumoItem(stringResource(id = R.string.subtotal_1), detalhes.subtotal1.toString())
        DottedLineComponent()
        ResumoItem(stringResource(id = R.string.desconto_em_produtos), detalhes.valorDescontoProdutos.toString())
        DottedLineComponent()
        ResumoItem(stringResource(id = R.string.subtotal_2), detalhes.subtotal2.toString())
        DottedLineComponent()
        ResumoItem(stringResource(id = R.string.desconto_na_venda), detalhes.valorDescontoVenda.toString())
        DottedLineComponent()
        ResumoItem(stringResource(id = R.string.valor_total), detalhes.valorTotal.toString(), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ResumoItem(
    titulo: String,
    valor: String,
    fontStyle: FontStyle = FontStyle.Normal,
    fontWeight: FontWeight = FontWeight.Normal
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 6.dp)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontStyle = fontStyle,
                fontWeight = fontWeight
            )
        }
        Column(
            modifier = Modifier
                .padding(vertical = 6.dp)
        ) {
            Text(
                valor,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontStyle = fontStyle,
                fontWeight = fontWeight
            )
        }
    }
}

@Preview
@Composable
fun ModalResumoVendaPreview() {
    ModalResumoVenda({}, VendaDetalhes(1, 1111.0, 1.0, 1.0, 1.0, 1.0, 1.0))
}