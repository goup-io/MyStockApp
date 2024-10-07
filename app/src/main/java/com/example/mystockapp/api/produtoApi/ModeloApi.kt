package com.example.mystockapp.api.produtoApi

import com.example.mystockapp.models.produtos.Modelo
import com.example.mystockapp.models.produtos.ModeloReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ModeloApi {
    @GET("api/v1/modelos")
    suspend fun getModelos(): Response<List<Modelo>>

    @POST("api/v1/modelos")
    suspend fun createModelo(@Body modeloReq: ModeloReq): Response<Modelo>
}