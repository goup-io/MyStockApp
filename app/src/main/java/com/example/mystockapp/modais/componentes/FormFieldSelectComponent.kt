package com.example.mystockapp.modais.componentes

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
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
fun SelectField(
    label: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 15.sp),
    labelFontSize: TextUnit = 10.sp,
    borderColor: Color = Color(0xFF355070),
    backgroundColor: Color = Color.White,
    fieldSize: Dp = 20.dp,
    disabled: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = label,
            fontSize = labelFontSize,
            fontWeight = FontWeight.Normal
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
                .height(fieldSize)
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .border(1.dp, borderColor, RoundedCornerShape(5.dp))
                .background(backgroundColor, RoundedCornerShape(5.dp))
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .clickable { if (!disabled) expanded = !expanded },
                textStyle = textStyle,
                singleLine = true,
                enabled = !disabled,
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Icon"
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = backgroundColor,
                    disabledIndicatorColor = borderColor,
                    unfocusedIndicatorColor = borderColor,
                    focusedIndicatorColor = borderColor
                )
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}
