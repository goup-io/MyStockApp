package com.example.mystockapp.telas.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.R
import com.example.mystockapp.models.Produto
import com.example.mystockapp.ui.theme.Cores
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

@Composable
fun Table(
    listItems : List<Produto>
) {
//    val listColumns: List<String> = Produto::class.memberProperties.map { it.name }.map { it.take(7) }.filter { !it.startsWith('$') }
    val listColumns = listOf("Código", "Nome", "Preço", "Qtd", "Ver mais")

    println(listColumns[listColumns.size - 1])
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clip(RoundedCornerShape(4.dp)).border(width = 2.dp, color = Cores.AzulBackground)
    ) {
        Row (modifier = Modifier.background(Cores.AzulBackground)){
            listColumns.forEach { column ->
                Text(text = column, textAlign = TextAlign.Center, color = Color.White, modifier = Modifier.weight(1f).padding(vertical = 4.dp, horizontal = 2.dp))
            }
        }
        listItems.forEach{ produto ->
            Row (verticalAlignment = Alignment.CenterVertically){
                Text(text = produto.codigo, textAlign = TextAlign.Center, modifier = Modifier.weight(1f).padding(vertical = 4.dp, horizontal = 2.dp))
                Text(text = produto.nome, textAlign = TextAlign.Center, modifier = Modifier.weight(1f).padding(vertical = 4.dp, horizontal = 2.dp))
                Text(text = produto.preco.toString(), textAlign = TextAlign.Center, modifier = Modifier.weight(1f).padding(vertical = 4.dp, horizontal = 2.dp))
                Text(text = produto.quantidade.toString(), textAlign = TextAlign.Center, modifier = Modifier.weight(1f).padding(vertical = 4.dp, horizontal = 2.dp))
                Text(text = "ICON", textAlign = TextAlign.Center, modifier = Modifier.weight(1f).padding(vertical = 4.dp, horizontal = 2.dp))

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewTable(){
    val lista = listOf(Produto("codigo", "nome", "cor", 10.0, "modelo", 1, 40 ))
    Table(lista)
}