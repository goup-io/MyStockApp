package com.example.mystockapp.telas.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mystockapp.modais.SideMenu
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDrawer(titulo: String, content: @Composable () -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f) // Ocupa 50% da largura da tela
                    .fillMaxHeight()
                    .background(Color(0xFF355070))
            ) {
                SideMenu()
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF355070))
        ) {
            Column {
                Header(
                    titulo = titulo,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
                content()
            }
        }
    }
}