package com.example.mystockapp.api.produtoApi
import com.example.mystockapp.models.produtos.Tipo
import retrofit2.Response;
import retrofit2.http.GET;

interface TipoApi {
    @GET("api/v1/tipos")
    suspend fun getTipos(): Response<List<Tipo>>
}
