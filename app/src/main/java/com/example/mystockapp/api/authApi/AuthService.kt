package com.example.mystockapp.api.authApi

import com.example.mystockapp.models.auth.LoginRequest
import com.example.mystockapp.models.auth.LoginResponse

class AuthService(private val api: AuthApi) {
    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = api.login(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Erro desconhecido"))
            }  else if (response.code() == 403) {
                Result.failure(Exception("Usuário ou senha inválidos"))
            } else {
                Result.failure(Exception("Erro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}