package com.example.mystockapp.api.authApi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.models.auth.LoginRequest
import com.example.mystockapp.models.auth.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authService: AuthService) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(user: String, senha: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = authService.login(LoginRequest(user, senha))
            if (result.isSuccess) {
                result.getOrNull()?.let { response ->
                    RetrofitInstance.updateToken(response.token)
                    _loginState.value = LoginState.Success(response)
                }
            } else {
                _loginState.value = LoginState.Error(result.exceptionOrNull()?.message ?: "Erro desconhecido")
            }
        }
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val response: LoginResponse) : LoginState()
        data class Error(val message: String) : LoginState()
    }
}