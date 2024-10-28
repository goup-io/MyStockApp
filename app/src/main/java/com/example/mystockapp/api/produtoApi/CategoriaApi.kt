package com.example.mystockapp.api.produtoApi

import com.example.mystockapp.models.produtos.Categoria
import retrofit2.Response
import retrofit2.http.GET

interface CategoriaApi {
    @GET("api/v1/categorias")
    suspend fun getCategorias(): Response<List<Categoria>>
}