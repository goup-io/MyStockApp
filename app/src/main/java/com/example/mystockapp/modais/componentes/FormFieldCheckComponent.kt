package com.example.mystockapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormFieldCheck(
    label: String,
    isChecked: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
    labelFontSize: Float = 10f,
    labelColor: Color = Color.Black,
    checkedColor: Color = Color(0xFF355070),
    uncheckedColor: Color = Color.White
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(35.dp) // Aumenta a altura do Row para incluir o espaço extra
                .fillMaxWidth()
                .padding(start = 0.dp, end = 0.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    if (!disabled) {
                        onCheckedChange(it)
                    }
                                  },
                modifier = Modifier
                    .scale(1.5f) // Escala o tamanho do checkbox
                    .padding(end = 2.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = checkedColor,
                    uncheckedColor = uncheckedColor,
                    checkmarkColor = Color.White,
                )
            )
            Text(
                text = label,
                fontSize = labelFontSize.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = labelFontSize.sp,
                color = labelColor,
                modifier = Modifier.padding(0.dp)
            )
        }
    }
}
