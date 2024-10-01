package com.example.mystockapp.api.authApi

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthViewModelFactory(
    private val authService: AuthService,
    private val context: Context // Adicione o contexto como parâmetro
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authService, context) as T // Passe o contexto para o ViewModel
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
