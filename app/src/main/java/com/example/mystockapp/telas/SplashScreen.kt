package com.example.mystockapp.telas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mystockapp.R
import com.example.mystockapp.telas.viewModels.LoginViewModel
import com.example.mystockapp.ui.theme.MyStockAppTheme
import com.example.mystockapp.util.DataStoreUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SplashScreen : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStockAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    TelaSplashScreen()
                }
            }
        }

        // Criação da ViewModel
        val loginViewModel: LoginViewModel = ViewModelProvider(
            this,
            LoginViewModel.LoginViewModelFactory(this)
        )[LoginViewModel::class.java]

        lifecycleScope.launch {
            // Recuperar os valores do DataStore
            val userData = DataStoreUtils.recuperarUsuarioOffline(this@SplashScreen)
            val login = userData["login"] ?: ""
            val senha = userData["senha"] ?: ""
            val token = userData["token"] ?: ""
            Log.d("SplashScreen", "Login: $login")

            // Navegação após recuperar os dados
            Handler(Looper.getMainLooper()).postDelayed({
                if (login.isNotBlank()) {
                    Log.d("SplashScreen", "Dentro do IF Login: $login")
                    loginViewModel.login(login, senha)
                    val intent = Intent(this@SplashScreen, PreVenda::class.java)
                    startActivity(intent)
                    finish()
                    return@postDelayed
                } else {
                    // Navegação para tela de login
                    val intent = Intent(this@SplashScreen, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 3000)
        }
    }
}

@Composable
fun TelaSplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF355070)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_mystock),
            contentDescription = "Ícone MyStock",
            modifier.size(300.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview2() {
    MyStockAppTheme {
        TelaSplashScreen()
    }
}