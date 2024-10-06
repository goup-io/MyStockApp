package com.example.mystockapp.modais.componentes

import android.icu.text.ListFormatter.Width
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectField(
    label: String,
    selectedOption: String,
    options: List<T>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 10.sp), // Fonte ajustada para caber em 20.dp
    labelFontSize: TextUnit = 10.sp,
    disabled: Boolean = false,
    error: Boolean = false,
    borderColor: Color = Color(0xFF355070),
    containerColor: Color = if (disabled) Color(0xFFECECEC) else Color.White,
    fieldHeight: Dp = 20.dp, // Altura fixada em 20.dp
    width: Dp = 200.dp,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.width(width),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = label,
            fontSize = labelFontSize,
            fontWeight = FontWeight.Normal
        )
        Box(
            modifier = Modifier
                .width(width)
                .clipToBounds()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(fieldHeight) // Garantindo a altura de 20.dp
                    .border(1.dp, if (error) Color.Red else borderColor, RoundedCornerShape(5.dp))
                    .background(containerColor, RoundedCornerShape(5.dp))
                    .clickable { if (!disabled) expanded = !expanded }
            ) {
                // Exibindo a opção selecionada
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(fieldHeight),
                    contentAlignment = androidx.compose.ui.Alignment.CenterStart // Centralizando o texto verticalmente
                ) {
                    Text(
                        text = if (selectedOption.isNotEmpty()) selectedOption else "Selecione uma opção",
                        style = textStyle,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    // Ícone de dropdown
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier
                            .align(androidx.compose.ui.Alignment.CenterEnd)
                            .padding(end = 8.dp)
                            .size(16.dp) // Ícone pequeno para caber no campo de 20.dp
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(width)
                        .background(containerColor),
                    offset = androidx.compose.ui.unit.DpOffset(
                        0.dp,
                        fieldHeight - 15.dp
                    )
                ) {
                    // Definir uma altura máxima para o menu e adicionar uma barra de rolagem
                    Column(
                        modifier = Modifier
                            .height(200.dp) // Ajuste a altura máxima conforme necessário
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                    ) {
                        options.forEachIndexed { index, option ->
                            DropdownMenuItem(
                                text = { Text(option.toString(), fontSize = 10.sp) },
                                onClick = {
                                    onOptionSelected(option.toString())
                                    expanded = false
                                }
                            )
                            // Adiciona um Divider entre as opções, exceto na última
                            if (index < options.size - 1) {
                                Divider(
                                    thickness = 0.5.dp,
                                    color = Color.Gray.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}
