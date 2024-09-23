package com.example.mystockapp.api.produtoApi

import android.util.Log
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.models.ProdutoTable
import retrofit2.HttpException
import java.io.IOException

class ProdutoService(private val produtoApi: ProdutoApi) {
    suspend fun fetchProdutosTabela(): List<ProdutoTable> {
        return try {
            val response = produtoApi.getProdutos()
            if (response.isSuccessful) {
                Log.d("ProdutoService", "API Response: ${response.body()}")
                response.body() ?: emptyList()
            } else {
                Log.e("ProdutoService", "API Error: ${response.code()} - ${response.message()}")
                throw ApiException(response.code(), response.message())
            }
        } catch (e: IOException) {
            Log.e("ProdutoService", "Network Error: ${e.message}")
            throw NetworkException("Network error occurred", e)
        } catch (e: HttpException) {
            Log.e("ProdutoService", "HTTP Error: ${e.code()} - ${e.message()}")
            throw ApiException(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("ProdutoService", "Unexpected Error: ${e.message}")
            throw GeneralException("An unexpected error occurred", e)
        }
    }
}