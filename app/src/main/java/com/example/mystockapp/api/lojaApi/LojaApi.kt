package com.example.mystockapp.api.lojaApi

import com.example.mystockapp.models.lojas.Loja
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LojaApi {
    @GET("api/v1/lojas/{idLoja}")
    suspend fun getLojaById(@Path("idLoja") idLoja: Int): Response<Loja>
}