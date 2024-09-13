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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystockapp.ui.theme.MyStockAppTheme

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
                            "Você deve aceitar os termos para continuar usando o app.",
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
                text = "Política de Privacidade – MyStock",
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
                text = """
                **1. Coleta de Dados**
                Coletamos dados pessoais fornecidos pelos usuários, como nome, e-mail e informações de contato, para fins de cadastro e uso do aplicativo. Além disso, dados relacionados às transações comerciais e ao gerenciamento de estoque também são armazenados.
                
                **2. Uso dos Dados**
                Os dados coletados são utilizados exclusivamente para a operação do MyStock, oferecendo controle de estoque em tempo real, emissão de relatórios e insights de vendas. Não compartilhamos informações pessoais com terceiros sem o consentimento do usuário, exceto quando exigido por lei.
                
                **3. Segurança dos Dados**
                O MyStock implementa medidas de segurança técnicas e organizacionais adequadas para proteger os dados pessoais contra acessos não autorizados, perdas ou qualquer forma de tratamento ilícito.
                
                **4. Direitos dos Usuários**
                Em conformidade com a LGPD, os usuários podem solicitar acesso, correção, exclusão ou a portabilidade de seus dados pessoais. O usuário também pode revogar o consentimento para o uso dos seus dados a qualquer momento, mediante solicitação.
                
                **5. Armazenamento dos Dados**
                Os dados são armazenados em servidores seguros, localizados em conformidade com as diretrizes da LGPD, e são mantidos pelo tempo necessário para o cumprimento das finalidades descritas, ou conforme exigido por lei.
                
                **6. Contato**
                Para exercer seus direitos ou obter mais informações sobre como seus dados são tratados, entre em contato com nossa equipe pelo e-mail: goup.contactus@gmail.com.
                """.trimIndent(),
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
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Aceitar e continuar", color = Color(0xFF355070))
            }

            // Botão de recusar
            Button(
                onClick = onDecline,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF06677)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Text(text = "Recusar", color = Color.White)
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
