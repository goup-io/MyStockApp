package com.example.mystockapp.api.produtoApi

import com.example.mystockapp.models.produtos.Modelo
import retrofit2.Response
import retrofit2.http.GET

interface ModeloApi {
    @GET("api/v1/modelos")
    suspend fun getModelos(): Response<List<Modelo>>
}