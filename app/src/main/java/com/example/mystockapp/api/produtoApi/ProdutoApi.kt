package com.example.mystockapp.api.produtoApi

import com.example.mystockapp.models.produtos.AdicionarEstoqueReq
import com.example.mystockapp.models.produtos.Produto
import com.example.mystockapp.models.produtos.ProdutoAdicionarQuantidadeRes
import com.example.mystockapp.models.produtos.ProdutoCreate
import com.example.mystockapp.models.produtos.ProdutoEdit
import com.example.mystockapp.models.produtos.ProdutoEditarGet
import com.example.mystockapp.models.produtos.ProdutoTable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Path

interface ProdutoApi {
    @GET("api/v1/etps/filtro")
    suspend fun getProdutosByLoja(@Query("id_loja") idLoja: Int): Response<List<ProdutoTable>>

    @GET("/api/v1/etps/editar/{id}")
    suspend fun getProdutoEditarById(@Path("id") id: Int): Response<ProdutoEditarGet>

    @GET("/api/v1/etps/{id}")
    suspend fun getEtpById(@Path("id") id: Int): Response<Produto>

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

    @POST("/api/v1/produtos")
    suspend fun createEtp(@Body produtoCreate: ProdutoCreate): Response<Produto>



    @GET("api/v1/etps/filtro")
    suspend fun searchProdutos(
        @Query("pesquisa") searchTerm: String,
        @Query("id_loja") idLoja: Int
    ): Response<List<ProdutoTable>>



    @GET("/api/v1/etps/filtro")
    suspend fun getProdutosByFiltros(
        @Query("id_Loja") idLoja: Int,  // Obrigatório
        @Query("modelo") modelo: String? = null,  // Omitido se for null
        @Query("tamanho") tamanho: Int? = null,   // Omitido se for null
        @Query("cor") cor: String? = null,        // Omitido se for null
        @Query("precoMinimo") precoMin: Double? = null, // Omitido se for null
        @Query("precoMaximo") precoMax: Double? = null  // Omitido se for null
    ): Response<List<ProdutoTable>>


}