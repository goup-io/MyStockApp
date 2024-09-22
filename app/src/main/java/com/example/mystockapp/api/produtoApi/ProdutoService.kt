package com.example.mystockapp.api.produtoApi

import android.util.Log
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.models.produtos.ProdutoEdit
import com.example.mystockapp.models.produtos.ProdutoEditarGet
import com.example.mystockapp.models.produtos.ProdutoTable
import retrofit2.HttpException
import java.io.IOException
class ProdutoService(private val produtoApi: ProdutoApi) {
    suspend fun fetchProdutosTabela(idLoja: Int): List<ProdutoTable> {
        return try {
            val response = produtoApi.getProdutosByLoja(idLoja)

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

    suspend fun fetchProdutosParaEditarTabela(idProduto: Int): ProdutoEditarGet {
        return try {
            val response = produtoApi.getProdutoEditarById(idProduto)

            if (response.isSuccessful) {
                Log.d("ProdutoService", "API Response: ${response.body()}")
                response.body() ?: throw ApiException(404, "Produto não encontrado")
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

    suspend fun editarEtp(idProduto: Int, produtoEdit: ProdutoEdit): ProdutoEditarGet {
        return try {
            val response = produtoApi.updateEtp(idProduto, produtoEdit)

            if (response.isSuccessful) {
                Log.d("ProdutoService", "API Response: ${response.body()}")
                response.body() ?: throw ApiException(500, "Não foi possível atualizar")
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

    suspend fun deleteEtp(idProduto: Int): Int {
        return try {
            val response = produtoApi.deleteEtp(idProduto)

            if (response.isSuccessful) {
                Log.d("ProdutoService", "API Response: ${response.body()}")
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
