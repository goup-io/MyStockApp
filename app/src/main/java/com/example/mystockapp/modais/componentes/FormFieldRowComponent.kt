package com.example.mystockapp.modais.componentes


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormFieldRowComponent(
    label: String,
    textValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 15.sp),
    labelFontSize: TextUnit = 12.sp,
    disabled: Boolean = false,
    borderColor: Color = Color(0xFF355070),
    backgroundColor: Color = if (disabled) Color(0xFFECECEC) else Color.White,
    placeholder: String = "",
    width: Dp = 200.dp,
    height: Dp = 20.dp,
    fieldType: KeyboardType = KeyboardType.Text,
    error: Boolean = false
) {
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
                .height(height)
                .border(1.dp, if (error) Color.Red else borderColor, RoundedCornerShape(5.dp))
                .background(backgroundColor, RoundedCornerShape(5.dp))
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = { onValueChange(it) },
                enabled = !disabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp, horizontal = 4.dp),
                textStyle = textStyle.copy(
                    fontSize = 12.sp,
                    lineHeight = 12.sp
                ),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = fieldType),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (textValue.isEmpty() && placeholder.isNotEmpty()) {
                            Text(
                                text = placeholder,
                                style = TextStyle(fontSize = 12.sp, color = Color.Gray),
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewFormFieldRowComponent() {
    FormFieldRowComponent(
        label = "Codigo:",
        textValue = "ateataeet",
        fieldType = KeyboardType.Number,
        onValueChange = { },
        error = false
    )
}