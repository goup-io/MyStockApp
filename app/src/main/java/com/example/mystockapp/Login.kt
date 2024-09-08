package com.example.mystockapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.ui.theme.MyStockAppTheme

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStockAppTheme {
                LoginScreen { navigateToMainActivity() }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, BipScreen::class.java)
        startActivity(intent)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF355070)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_logo_mystock_tp2),
                contentDescription = null,
                modifier = Modifier
                    .size(206.dp, 158.dp)
                    .padding(top = 4.dp)
            )

            Text(
                text = "Login",
                color = Color.White,
                style = TextStyle(
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 56.dp)
            )

            Text(
                text = "Usuário:",
                color = Color.White,
                style = TextStyle(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(0.8f)
            )

            TextField(
                value = emailState,
                onValueChange = { emailState = it },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(55.dp)
                    .padding(top = 4.dp)
                    .border(
                        1.dp,
                        if (showError && emailState.isEmpty()) Color.Red else Color.Transparent,
                        RoundedCornerShape(50.dp)
                    ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                placeholder = { Text("usuário") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_user),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                shape = RoundedCornerShape(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            Text(
                text = "Senha:",
                color = Color.White,
                style = TextStyle(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.8f)
            )

            TextField(
                value = passwordState,
                onValueChange = { passwordState = it },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(55.dp)
                    .padding(top = 4.dp)
                    .border(
                        1.dp,
                        if (showError && passwordState.isEmpty()) Color.Red else Color.Transparent,
                        RoundedCornerShape(50.dp)
                    ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                placeholder = { Text("*****") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_lock),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                },
                shape = RoundedCornerShape(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            if (showError) {
                Text(
                    text = "Por favor, preencha todos os campos.",
                    color = Color(0xFFEF233C),
                    modifier = Modifier.padding(top = 8.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Button(
                onClick = {
                    if (emailState.isNotEmpty() && passwordState.isNotEmpty()) {
                        onLoginSuccess()
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 28.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD0D4F0)
                ),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(text = "Entrar", color = Color(0xFF355070), fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.mipmap.goup_rodape_login),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(208.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview3() {
    MyStockAppTheme {
        LoginScreen {}
    }
}