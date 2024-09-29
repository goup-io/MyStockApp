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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mystockapp.models.vendas.VendaDetalhes


@Composable
fun ModalResumoVenda(onDismissRequest: () -> Unit, detalhes: VendaDetalhes) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier =
            Modifier.fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(15.dp),
        ){
            Column (
                modifier = Modifier.padding(16.dp)
            ){
                ModalHeaderComponent(onDismissRequest,"Resumo da Venda")
                ResumoLista(detalhes)
            }
        }
    }
}

@Composable
fun ResumoLista(detalhes: VendaDetalhes){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ResumoItem("Total de Itens",detalhes.totalItens.toString())
        DottedLineComponent()
        ResumoItem("Subtotal 1",detalhes.subtotal1.toString())
        DottedLineComponent()
        ResumoItem("Desconto em Produtos",detalhes.valorDescontoProdutos.toString())
        DottedLineComponent()
        ResumoItem("Subtotal 2",detalhes.subtotal2.toString())
        DottedLineComponent()
        ResumoItem("Desconto na Venda",detalhes.valorDescontoVenda.toString())
        DottedLineComponent()
        ResumoItem("Valor Total",detalhes.valorTotal.toString(), fontWeight = FontWeight.Bold)
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
                fontSize = 12.sp,
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
                fontSize = 12.sp,
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
    ModalResumoVenda({}, VendaDetalhes(1, 1.0, 1.0, 1.0, 1.0, 1.0))
}