package com.example.mystockapp.api.produtoApi

import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.models.produtos.Modelo
import com.example.mystockapp.models.produtos.ModeloReq
import retrofit2.HttpException
import java.io.IOException

class ModeloService(private val modeloApi: ModeloApi) {
    suspend fun fetchModelos(): List<Modelo> {
        return try {
            val response = modeloApi.getModelos()

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

    suspend fun createModelo(modeloReq: ModeloReq): Modelo {
        return try {
            val response = modeloApi.createModelo(modeloReq)

            if (response.isSuccessful) {
                response.body() ?: Modelo(
                    id = 0,
                    nome = "",
                    tipo = "",
                    categoria = "")
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