package com.example.mystockapp.modais


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mystockapp.ui.theme.MyStockAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStockAppTheme() {
                AddProductToStock()
            }
        }
    }
}


@Composable
fun AddProductToStock() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(10.dp))
            .shadow(4.dp)
            .padding(11.dp, 9.dp, 9.dp, 7.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add Produto no Estoque",
                fontSize = 14.sp,
                color = Color.Black
            )
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .padding(2.dp)
                    .background(
                        color = Color(0xFFEF233C),
                        shape = RoundedCornerShape(3.dp)
                    )
                    .clickable { /* TODO: Handle close */ },
                contentAlignment = Alignment.Center
            ) {
                Text("X", color = Color.White, fontSize = 10.sp)
            }
        }

        ProductTable()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { /* TODO: Handle delete */ },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.shadow(4.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFEF233C)),
            ) {
                Text("Excluir", color = Color.White, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(24.dp))
            Button(
                onClick = { /* TODO: Handle edit */ },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.shadow(4.dp)
            ) {
                Text("Editar", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ProductTable() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF355070), RoundedCornerShape(5.dp))
            .padding(top = 0.dp)
    ) {
        TableHeader()
        ProductRow(backgroundColor = Color(0xFFE7E7E7))
        ProductRow(backgroundColor = Color(0xFFD0D4F0))
        ProductRow(backgroundColor = Color(0xFFE7E7E7))
        ProductRow(backgroundColor = Color(0xFFD0D4F0))
        ProductRow(backgroundColor = Color(0xFFE7E7E7))
    }
}

@Composable
fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF355070), RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
            .padding(6.dp, 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Código", modifier = Modifier.width(30.dp), textAlign = TextAlign.Center, color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.SemiBold)
        Text("Nome", modifier = Modifier.width(30.dp), textAlign = TextAlign.Center, color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.SemiBold)
        Text("Modelo", modifier = Modifier.width(30.dp), textAlign = TextAlign.Center, color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.SemiBold)
        Text("Preço", modifier = Modifier.width(30.dp), textAlign = TextAlign.Center, color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.SemiBold)
        Text("Tamanho", modifier = Modifier.width(35.dp), textAlign = TextAlign.Center, color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.SemiBold)
        Text("Cor", modifier = Modifier.width(30.dp), textAlign = TextAlign.Center, color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.SemiBold)
        Text("Loja", modifier = Modifier.width(30.dp), textAlign = TextAlign.Center, color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.SemiBold)
        Text("N. Itens", modifier = Modifier.width(30.dp), textAlign = TextAlign.Center, color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.SemiBold)
        Text("Add", modifier = Modifier.width(30.dp), textAlign = TextAlign.Center, color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.SemiBold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRow(backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .border(1.dp, Color(0xFF355070))
            .padding(6.dp, 9.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("01234", modifier = Modifier.width(30.dp), textAlign = TextAlign.Center, fontSize = 8.sp)
        Text("Triple Black", modifier = Modifier.width(30.dp),textAlign = TextAlign.Center,  fontSize = 8.sp)
        Text("Air Force", modifier = Modifier.width(30.dp),textAlign = TextAlign.Center,  fontSize = 8.sp)
        Text("300,00", modifier = Modifier.width(30.dp),textAlign = TextAlign.Center,  fontSize = 8.sp)
        Text("37", modifier = Modifier.width(30.dp),textAlign = TextAlign.Center,  fontSize = 8.sp)
        Text("Preto", modifier = Modifier.width(30.dp),textAlign = TextAlign.Center,  fontSize = 8.sp)
        Text("Loja 1", modifier = Modifier.width(30.dp),textAlign = TextAlign.Center,  fontSize = 8.sp)
        Text("20", modifier = Modifier.width(30.dp),textAlign = TextAlign.Center, fontSize = 8.sp)

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = "5",
                onValueChange = { /* TODO: Handle value change */ },
                modifier = Modifier
                    .size(width = 20.dp, height = 20.dp) // Adjust size of the TextField
                    .border(1.dp, Color(0xFF355070), RoundedCornerShape(50)) // Circular border without background
                    .padding(0.dp), // Remove padding to fit the content properly
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 8.sp,
                    color = Color.Black, // Set text color for visibility
                    textAlign = TextAlign.Center // Center the text
                ),
                singleLine = true // Ensure single line input
            )
            Spacer(modifier = Modifier.width(4.dp)) // Space between TextField and "+"
            Text("+", fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(start = 2.dp)) // Ensure text visibility
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyStockAppTheme() {
        AddProductToStock()
    }
}