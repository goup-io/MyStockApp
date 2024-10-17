package com.example.mystockapp.api.vendaApi

import android.util.Log
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.models.vendas.TipoVendasDataClass
import com.example.mystockapp.models.vendas.VendaPost
import com.example.mystockapp.models.vendas.VendaRes
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class VendaService(private val vendaApi: VendaApi) {
    suspend fun createVenda(vendaReq: VendaPost): Response<VendaRes> {
        return try {
            val response = vendaApi.createVenda(vendaReq)

            if (response.isSuccessful) {
                Log.d("VendaService", "API Response: ${response.body()}")
                response
            } else {
                val errorMessage = response.errorBody()?.string() ?: response.message() ?: "Erro desconhecido"
                Log.e("VendaService", "API Error: ${response.code()} - $errorMessage")
                throw ApiException(response.code(), errorMessage)
            }
        } catch (e: IOException) {
            Log.e("VendaService", "Network Error: ${e.message}")
            throw NetworkException("Network error occurred", e)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message() ?: "Erro desconhecido"
            Log.e("VendaService", "HTTP Error: ${e.code()} - $errorMessage")
            if (e.code() == 403) {
                throw ApiException(e.code(), "Acesso negado: Token inválido ou sem permissão.")
            } else {
                throw ApiException(e.code(), errorMessage)
            }
        }
    }

    suspend fun getTiposVenda():List<TipoVendasDataClass> {
        return try {
            val response = vendaApi.getTiposVenda()

            if (response.isSuccessful) {
                Log.d("VendaService", "API Response: ${response.body()}")
                response.body() ?: emptyList()
            } else {
                val errorMessage = response.errorBody()?.string() ?: response.message() ?: "Erro desconhecido"
                Log.e("VendaService", "API Error: ${response.code()} - $errorMessage")
                throw ApiException(response.code(), errorMessage)
            }
        } catch (e: IOException) {
            Log.e("VendaService", "Network Error: ${e.message}")
            throw NetworkException("Network error occurred", e)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message() ?: "Erro desconhecido"
            Log.e("VendaService", "HTTP Error: ${e.code()} - $errorMessage")
            if (e.code() == 403) {
                throw ApiException(e.code(), "Acesso negado: Token inválido ou sem permissão.")
            } else {
                throw ApiException(e.code(), errorMessage)
            }
        }
    }
}