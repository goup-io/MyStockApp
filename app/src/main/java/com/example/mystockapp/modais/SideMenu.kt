package com.example.mystockapp.modais

import android.graphics.drawable.Icon
import android.view.MenuItem
import android.view.WindowInsets.Side
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SideMenu() {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.80f) // Ocupa 30% da largura da tela
            .background(Color(0xFF355070))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // User info at the top
        UserSection()

        Spacer(modifier = Modifier.height(32.dp)) // Aumenta o espaçamento

        // Menu items with Material Icons
        MenuItem(icon = Icons.Default.Menu, text = "Menu") {
            // Handle menu item click
        }

        MenuItem(icon = Icons.Default.Settings, text = "Estoque") {
            // Handle estoque item click
        }

        MenuItem(icon = Icons.Default.ShoppingCart, text = "Venda") {
            // Handle venda item click
        }

        MenuItem(icon = Icons.Default.MoreVert, text = "Bipe") {
            // Handle bipe item click
        }
    }
}

@Composable
fun UserSection() {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "User",
            tint = Color.White,
            modifier = Modifier.size(60.dp) // Ícone maior
        )


        Text(
            text = "User",
            fontSize = 20.sp,  // Texto um pouco maior
            color = Color.White,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Gerente",
            fontSize = 16.sp,  // Texto maior para o subtítulo
            color = Color.Gray
        )
    }
}

@Composable
fun MenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 26.dp)  // Mais espaçamento vertical
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(30.dp)  // Ícone um pouco maior
        )

        Spacer(modifier = Modifier.width(22.dp))  // Mais espaçamento entre ícone e texto

        Text(
            text = text,
            fontSize = 18.sp,  // Texto maior
            color = Color.White
        )
    }
}

@Preview
@Composable
fun PreviewSideMenu() {
    SideMenu()
}