package com.example.mystockapp.api.produtoApi

import com.example.mystockapp.models.produtos.AdicionarEstoqueReq
import com.example.mystockapp.models.produtos.ProdutoAdicionarQuantidadeRes
import com.example.mystockapp.models.produtos.ProdutoEdit
import com.example.mystockapp.models.produtos.ProdutoEditarGet
import com.example.mystockapp.models.produtos.ProdutoTable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Path

interface ProdutoApi {
    @GET("api/v1/etps/filtro")
    suspend fun getProdutosByLoja(@Query("id_loja") idLoja: Int): Response<List<ProdutoTable>>

    @GET("/api/v1/etps/editar/{id}")
    suspend fun getProdutoEditarById(@Path("id") id: Int): Response<ProdutoEditarGet>

    @PUT("/api/v1/etps/{id}")
    suspend fun updateEtp(
        @Path("id") id: Int,
        @Body produto: ProdutoEdit
    ): Response<ProdutoEditarGet>

    @DELETE("/api/v1/etps/{id}")
    suspend fun deleteEtp(@Path("id") id: Int): Response<Unit>

    @PATCH("/api/v1/etps/adicionar-estoque/{idLoja}")
    suspend fun adicionarEstoque(
        @Path("idLoja") idLoja: Int,
        @Query("soma") soma: Boolean,
        @Body produtos: List<AdicionarEstoqueReq>
    ): Response<List<ProdutoAdicionarQuantidadeRes>>
}