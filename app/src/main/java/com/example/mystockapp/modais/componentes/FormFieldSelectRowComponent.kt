package com.example.mystockapp.modais.componentes


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FormFieldSelectRowComponent(
    label: String,
    selectedOption: String,
    options: List<T>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
    labelFontSize: TextUnit = 12.sp,
    disabled: Boolean = false,
    error: Boolean = false,
    borderColor: Color = Color(0xFF355070),
    containerColor: Color = if (disabled) Color(0xFFECECEC) else Color.White,
    fieldHeight: Dp = 20.dp,
    width: Dp = 200.dp,
) {
    var expanded by remember { mutableStateOf(false) }

    Row (
        modifier = modifier.width(width),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = labelFontSize,
                fontWeight = FontWeight.Normal
            ),
        )
        Box(
            modifier = Modifier
                .width(width)
                .clipToBounds()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(fieldHeight)
                    .border(1.dp, if (error) Color.Red else borderColor, RoundedCornerShape(5.dp))
                    .background(containerColor, RoundedCornerShape(5.dp))
                    .clickable { if (!disabled) expanded = !expanded }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (selectedOption.isNotEmpty()) selectedOption else "Selecione...",
                        style = textStyle.copy(
                            fontSize = labelFontSize,
                            fontWeight = FontWeight.Normal,
                        ),
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier
                            .size(16.dp)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(width)
                        .background(containerColor),
                    offset = DpOffset(
                        0.dp,
                        fieldHeight - 15.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .height(200.dp)
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                    ) {
                        options.forEachIndexed { index, option ->
                            DropdownMenuItem(
                                text = { Text(option.toString(), fontSize = 12.sp) },
                                onClick = {
                                    onOptionSelected(option.toString())
                                    expanded = false
                                }
                            )
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

@Preview(showSystemUi = true)
@Composable
fun PreviewFormFieldSelectRowComponent(){
    FormFieldSelectRowComponent(
        label = "Selecione uma opção",
        selectedOption = "TESTE COM O TEXTO BEM GRANDE PRA TESTAR SE ESTÁ POR CIMA DO NEGÓCIO ALI 1",
        options = listOf("Opção 1", "Opção 2", "Opção 3"),
        onOptionSelected = {},
        modifier = Modifier.fillMaxWidth()
    )
}
