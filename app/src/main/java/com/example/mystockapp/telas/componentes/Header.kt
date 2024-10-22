package com.example.mystockapp.telas.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.R
import com.example.mystockapp.ui.theme.Cores

@Composable
fun Header(
    titulo: String,
    onMenuClick: () -> Unit // Adiciona um parâmetro para o clique no menu
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(52.dp)
            .padding(16.dp)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Botão de menu
        Button(
            onClick = onMenuClick, // Usa o parâmetro onMenuClick
            modifier = Modifier
                .width(30.dp)
                .height(55.dp)
                .clip(RoundedCornerShape(5.dp)),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, // Botão com cor transparente
                contentColor = Color.White // Cor do texto e ícones dentro do botão
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(id = R.mipmap.menu),
                contentDescription = stringResource(id = R.string.menu_description),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
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
            contentDescription = stringResource(id = R.string.logo_empresa_description),
            modifier = Modifier.size(40.dp)
        )
    }
}