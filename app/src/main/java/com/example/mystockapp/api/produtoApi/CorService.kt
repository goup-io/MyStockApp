package com.example.mystockapp.api.produtoApi

import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.models.produtos.Cor
import retrofit2.HttpException
import java.io.IOException

class CorService(private val corApi: CorApi) {
    suspend fun fetchCores(): List<Cor> {
        return try {
            val response = corApi.getCores()

            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                val errorMessage = response.errorBody()?.string() ?: response.message() ?: "Erro desconhecido"
                throw ApiException(response.code(), errorMessage)
            }
        } catch (e: IOException) {
            throw NetworkException("Network error occurred", e)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message() ?: "Erro desconhecido"
            if (e.code() == 403) {
                throw ApiException(e.code(), "Acesso negado: Token inválido ou sem permissão.")
            } else {
                throw ApiException(e.code(), errorMessage)
            }
        }
    }
}