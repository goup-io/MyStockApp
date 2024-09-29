package com.example.mystockapp.modais

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormField(
    label: String,
    textValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 15.sp),
    labelFontSize: TextUnit = 10.sp,
    disabled: Boolean = false,
    borderColor: Color = Color(0xFF355070),
    backgroundColor: Color = if (disabled) Color(0xFFECECEC) else Color.White,
    placeholder: String = "",
    width: Dp = 200.dp,
    height: Dp = 20.dp,
    fieldType: KeyboardType = KeyboardType.Text,
    error: Boolean = false
) {
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
                .height(height)
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .border(1.dp, if(error )Color.Red else borderColor, RoundedCornerShape(5.dp))
                .background(backgroundColor, RoundedCornerShape(5.dp))
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = { onValueChange(it) },
                enabled = !disabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                textStyle = TextStyle(fontSize = 12.sp, lineHeight = TextUnit.Unspecified),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = fieldType),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(backgroundColor, RoundedCornerShape(5.dp))
                            .padding(4.dp)
                    ) {
                        if (textValue.isEmpty() && placeholder.isNotEmpty()) {
                            Text(text = placeholder, style = textStyle.copy(color = Color.Gray))
                        }
                        innerTextField()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}