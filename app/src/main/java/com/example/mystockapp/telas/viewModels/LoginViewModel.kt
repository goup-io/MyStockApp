package com.example.mystockapp.telas.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.authApi.AuthService
import com.example.mystockapp.api.authApi.AuthViewModel.LoginState
import com.example.mystockapp.api.authApi.AuthViewModelFactory
import com.example.mystockapp.models.auth.LoginRequest
import com.example.mystockapp.util.DataStoreUtils
import kotlinx.coroutines.launch

class LoginViewModel(private val context: Context) : ViewModel() {

    val authService = AuthService(RetrofitInstance.authApi)
    val viewModelFactory = AuthViewModelFactory(authService, context)

    var messageError by mutableStateOf("")

    fun login(user: String, senha: String){
        viewModelScope.launch {
            val result = authService.login(LoginRequest(user, senha))
            if (result.isSuccess) {
                result.getOrNull()?.let { response ->
                    RetrofitInstance.updateToken(response.token)
                    // Salvar o idLoja no SharedPreferences
                    val sharedPreferences = context.getSharedPreferences("MyStockPrefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putInt("idLoja", response.idLoja).apply()
                    DataStoreUtils.salvarUsuarioOffline(user, senha, response.token, context)
                    Log.d("LoginViewModel", "Login: ${DataStoreUtils.login} e senha: ${DataStoreUtils.senha}")
                }
            } else {
                messageError = result.exceptionOrNull()?.message ?: "Erro desconhecido"
            }
        }
    }

    class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
