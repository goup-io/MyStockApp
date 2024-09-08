package com.example.mystockapp.modais

import android.content.pm.PackageManager
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
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SideMenu() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFF355070))
        // Espaçamento entre os itens
    ) {
        // User info at the top
        UserSection()

        Spacer(modifier = Modifier.height(1.dp)) // Aumenta o espaçamento

        // Menu items with Material Icons
        MenuItem(icon = Icons.Default.Menu, text = "Menu") {
            // Handle menu item click
        }

        MenuItem(icon = Icons.Default.Inventory, text = "Estoque") {
            // Handle estoque item click
        }

        MenuItem(icon = Icons.Default.ShoppingCart, text = "Venda") {
            // Handle venda item click
        }

        MenuItem(icon = Icons.Default.PhotoCamera, text = "Bipe") {
            // Handle bipe item click
        }
    }
}

@Composable
fun UserSection() {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 25.dp, start = 25.dp),
        ) {
            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = "User",
                tint = Color.White,
                modifier = Modifier.size(40.dp).fillMaxWidth(fraction = 0.8f)// Ícone maior
            )
            Text(
                modifier = Modifier.fillMaxWidth(fraction = 0.8f).padding(top = 10.dp, start = 4.dp),
                text = "User",
                fontSize = 18.sp, // Texto maior para o subtítulo
                color = Color.White,
                textAlign = TextAlign.Start
                ,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Gerente",
                fontSize = 18.sp, // Texto maior para o subtítulo
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalArrangement = Arrangement.Center // Centraliza verticalmente
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 4.dp,
            color = Color.White
        )
    }
    }

@Composable
fun MenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 26.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(40.dp).padding(start = 12.dp)  // Ícone um pouco maior
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