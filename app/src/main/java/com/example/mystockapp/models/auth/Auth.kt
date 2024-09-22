package com.example.mystockapp.models.auth

// Model para o request de login
data class LoginRequest(
    val user: String,
    val senha: String
)

// Model para a resposta da API
data class LoginResponse(
    val token: String,
    val idLoja: Int,
    val tipoLogin: String,
    val contexto: String
)

