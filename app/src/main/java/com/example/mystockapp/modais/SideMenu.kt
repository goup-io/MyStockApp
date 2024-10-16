package com.example.mystockapp.modais

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.telas.BipScreen
import com.example.mystockapp.telas.Estoque
import com.example.mystockapp.telas.Login
import com.example.mystockapp.telas.LoginScreen
import com.example.mystockapp.telas.PreVenda

@Composable
fun SideMenu(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFF355070))
    ) {
        // User info at the top
        UserSection()

        Spacer(modifier = Modifier.height(1.dp))

        MenuItem(icon = Icons.Default.Inventory, text = "Estoque") {
            val intent = Intent(context, Estoque::class.java)
            context.startActivity(intent)
        }

        MenuItem(icon = Icons.Default.ShoppingCart, text = "Venda") {
            // Handle venda item click
            val intent = Intent(context, PreVenda::class.java)
            context.startActivity(intent)
        }

        MenuItem(icon = Icons.Default.PhotoCamera, text = "Bipe") {
            // Handle bipe item click
            val intent = Intent(context, BipScreen::class.java)
            context.startActivity(intent)
        }

        // Menu items with Material Icons
        MenuItem(icon = Icons.Default.ExitToApp, text = "Sair") {
            // Handle menu item click
            val intent = Intent(context, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

    }
}

@Composable
fun UserSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp, start = 25.dp),
    ) {
        Icon(
            imageVector = Icons.Default.AccountBox,
            contentDescription = "User",
            tint = Color.White,
            modifier = Modifier
                .size(40.dp)
                .fillMaxWidth(fraction = 0.8f)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.8f)
                .padding(top = 10.dp, start = 4.dp),
            text = "User",
            fontSize = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Start,
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(fraction = 0.9f),
            text = "Gerente",
            fontSize = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalArrangement = Arrangement.Center
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
            modifier = Modifier
                .size(40.dp)
                .padding(start = 12.dp)
        )

        Spacer(modifier = Modifier.width(22.dp))

        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun PreviewSideMenu() {
    SideMenu(context = LocalContext.current)
}