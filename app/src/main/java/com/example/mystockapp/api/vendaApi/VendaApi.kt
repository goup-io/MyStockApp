package com.example.mystockapp.api.vendaApi

import com.example.mystockapp.models.vendas.TipoVendasDataClass
import com.example.mystockapp.models.vendas.VendaPost
import com.example.mystockapp.models.vendas.VendaRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface VendaApi {

    @POST("/api/v1/vendas")
    suspend fun createVenda(
        @Body vendaReq: VendaPost
    ): Response<VendaRes>

    @GET("/api/v1/tiposVenda")
    suspend fun getTiposVenda(): Response<List<TipoVendasDataClass>>
}