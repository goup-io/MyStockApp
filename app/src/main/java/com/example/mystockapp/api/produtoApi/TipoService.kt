package com.example.mystockapp.api.produtoApi


import android.util.Log
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.models.produtos.Tipo
import retrofit2.HttpException
import java.io.IOException

class TipoService(private val tipoApi: TipoApi) {
    suspend fun fetchTipos(): List<Tipo> {
        return try {
            val response = tipoApi.getTipos()

            if (response.isSuccessful) {
                Log.d("ProdutoService", "API Response: ${response.body()}")
                response.body() ?: emptyList()
            } else {
                val errorMessage = response.errorBody()?.string() ?: response.message() ?: "Erro desconhecido"
                Log.e("ProdutoService", "API Error: ${response.code()} - $errorMessage")
                throw ApiException(response.code(), errorMessage)
            }
        } catch (e: IOException) {
            Log.e("ProdutoService", "Network Error: ${e.message}")
            throw NetworkException("Network error occurred", e)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message() ?: "Erro desconhecido"
            Log.e("ProdutoService", "HTTP Error: ${e.code()} - $errorMessage")
            if (e.code() == 403) {
                throw ApiException(e.code(), "Acesso negado: Token inválido ou sem permissão.")
            } else {
                throw ApiException(e.code(), errorMessage)
            }
        }
    }
}
