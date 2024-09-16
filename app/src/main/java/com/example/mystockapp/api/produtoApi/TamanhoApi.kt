package com.example.mystockapp.api.produtoApi

import com.example.mystockapp.models.produtos.Tamanho
import retrofit2.Response
import retrofit2.http.GET

interface TamanhoApi {
    @GET("api/v1/tamanhos")
    suspend fun getTamanhos(): Response<List<Tamanho>>
}