package com.example.mystockapp.telas

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.R
import com.example.mystockapp.api.authApi.AuthViewModel
import com.example.mystockapp.api.authApi.AuthService
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.authApi.AuthViewModelFactory
import com.example.mystockapp.ui.theme.MyStockAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("MyStockPrefs", Context.MODE_PRIVATE)
        val termsAccepted = sharedPreferences.getBoolean("termsAccepted", false)

        if (!termsAccepted) {
            val intent = Intent(this, TermsLgpdActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContent {
                MyStockAppTheme {
                    LoginScreen { navigateToMainActivity() }
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, PreVenda::class.java)
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
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authService = AuthService(RetrofitInstance.authApi)
    val viewModelFactory = AuthViewModelFactory(authService, LocalContext.current)
    val viewModel: AuthViewModel = viewModel(factory = viewModelFactory)
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        when (loginState) {
            is AuthViewModel.LoginState.Success -> onLoginSuccess()
            is AuthViewModel.LoginState.Error -> {
                showError = true
                errorMessage = (loginState as AuthViewModel.LoginState.Error).message
            }
            else -> showError = false
        }
    }

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
                contentDescription = stringResource(id = R.string.logo_empresa_description),
                modifier = Modifier
                    .size(206.dp, 158.dp)
                    .padding(top = 4.dp)
            )

            Text(
                text = stringResource(id = R.string.login_title),
                color = Color.White,
                style = TextStyle(
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 56.dp)
            )

            Text(
                text = stringResource(id = R.string.user_label),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                singleLine = true,
                maxLines = 1,
                placeholder = { Text(stringResource(id = R.string.user_placeholder)) },
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
                text = stringResource(id = R.string.password_label),
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
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                singleLine = true,
                maxLines = 1,
                placeholder = { Text(stringResource(id = R.string.password_placeholder)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_lock),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                },
                trailingIcon = {
                    val image = if (passwordVisible)
                        painterResource(id = R.mipmap.ic_visibility_off)
                    else
                        painterResource(id = R.mipmap.ic_visibility)

                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            painter = image,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            var msgEmptyField = stringResource(id = R.string.error_message_empty_fields)

            Button(
                onClick = {
                    if (emailState.isNotEmpty() && passwordState.isNotEmpty()) {
                        viewModel.login(emailState, passwordState)
                    } else {
                        showError = true
                        errorMessage = msgEmptyField
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
                Text(text = stringResource(id = R.string.enter_button), color = Color(0xFF355070), fontSize = 22.sp)
            }

            if (showError) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFEF233C),
                    modifier = Modifier.padding(top = 8.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
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