package com.example.mystockapp.api.produtoApi

import com.example.mystockapp.models.produtos.Cor
import retrofit2.Response
import retrofit2.http.GET

interface CorApi {
    @GET("api/v1/cores")
    suspend fun getCores(): Response<List<Cor>>
}