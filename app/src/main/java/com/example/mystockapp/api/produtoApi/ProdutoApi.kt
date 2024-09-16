package com.example.mystockapp.api.produtoApi

import com.example.mystockapp.models.produtos.ProdutoTable
import retrofit2.Response
import retrofit2.http.GET

interface ProdutoApi {
    @GET("api/v1/etps/filtro?id_loja=1")
    suspend fun getProdutos(): Response<List<ProdutoTable>>
}