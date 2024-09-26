package com.example.mystockapp.api.lojaApi

import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.produtoApi.CategoriaApi
import com.example.mystockapp.models.lojas.Loja
import retrofit2.HttpException
import java.io.IOException

class LojaService(private val lojaApi: LojaApi) {
    suspend fun fetchLojaById(idLoja: Int): Loja {
        return try {
            val response = lojaApi.getLojaById(idLoja)

            if (response.isSuccessful) {
                response.body() ?: throw ApiException(404, "Loja não encontrada")
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