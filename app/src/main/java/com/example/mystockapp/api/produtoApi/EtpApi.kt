package com.example.mystockapp.api.produtoApi

import com.example.mystockapp.models.produtos.EtpRes
import retrofit2.http.GET
import retrofit2.http.Query

interface EtpApi {
    @GET("/api/v1/etps")
    suspend fun listar(): List<EtpRes>

    @GET("/api/v1/etps/buscar/filtro")
    suspend fun buscarPorCodigo(
        @Query("pesquisa") codigo: String,
        @Query("id_loja") idLoja: Int
    ): EtpRes
}