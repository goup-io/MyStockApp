package com.example.mystockapp.telas.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.R
import com.example.mystockapp.telas.ui.theme.Cores

@Composable
fun Header(
    titulo : String,
    onClick : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(16.dp)
            .background(Cores.AzulBackground),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Botão de menu com tamanho ajustado
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(50.dp) // Aumentei o tamanho do botão para 50.dp
        ) {
            Image(
                painter = painterResource(id = R.mipmap.menu),
                contentDescription = "menu",
                modifier = Modifier.run { size(30.dp) } // Aumentei o tamanho da imagem
            )
        }

        // Título no centro
        Text(
            text = titulo,
            color = Color.White,
            fontSize = 20.sp
        )

        // Imagem da logo da empresa na direita
        Image(
            painter = painterResource(id = R.mipmap.mystock),
            contentDescription = "Logo da Empresa",
            modifier = Modifier.size(40.dp)
        )
    }
}