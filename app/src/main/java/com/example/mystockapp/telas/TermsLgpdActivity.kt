package com.example.mystockapp.telas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.ui.theme.MyStockAppTheme
import com.example.mystockapp.R

class TermsLgpdActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStockAppTheme {
                TermsScreen(
                    onAccept = {
                        saveTermsAccepted()
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onDecline = {
                        Toast.makeText(
                            this,
                            getString(R.string.decline_message),
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                )
            }
        }
    }

    private fun saveTermsAccepted() {
        val sharedPreferences = getSharedPreferences("MyStockPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("termsAccepted", true)
        editor.apply()
    }
}

@Composable
fun TermsScreen(onAccept: () -> Unit, onDecline: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF355070))
            .padding(16.dp)
    ) {
        // Título da política
        Column {
            Text(
                text = stringResource(id = R.string.terms_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

        // Bloco branco com o texto da política
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .background(Color.White, shape = RoundedCornerShape(15.dp))
                .padding(16.dp)
        ) {
            // Texto da política de privacidade
            Text(
                text = stringResource(id = R.string.terms_text),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Justify
            )
        }

        // Coluna para os botões
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(15.dp))

            // Botão de aceitar
            Button(
                onClick = onAccept,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0D4F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.accept_button), color = Color(0xFF355070))
            }

            // Botão de recusar
            Button(
                onClick = onDecline,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF06677)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Text(text = stringResource(id = R.string.decline_button), color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTermsScreen() {
    MyStockAppTheme {
        TermsScreen(onAccept = {}, onDecline = {})
    }
}

