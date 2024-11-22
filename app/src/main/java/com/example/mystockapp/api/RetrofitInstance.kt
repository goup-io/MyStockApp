package com.example.mystockapp.api

import com.example.mystockapp.api.authApi.AuthApi
import com.example.mystockapp.api.lojaApi.LojaApi
import com.example.mystockapp.api.produtoApi.CategoriaApi
import com.example.mystockapp.api.produtoApi.CorApi
import com.example.mystockapp.api.produtoApi.EtpApi
import com.example.mystockapp.api.produtoApi.ModeloApi
import com.example.mystockapp.api.produtoApi.ProdutoApi
import com.example.mystockapp.api.produtoApi.TamanhoApi
import com.example.mystockapp.api.produtoApi.TipoApi
import com.example.mystockapp.api.vendaApi.VendaApi
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitInstance {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var token: String? = null

    val headerInterceptor = Interceptor { chain ->
        val originalRequest: Request = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        token?.let {
            if (!originalRequest.url.toString().contains("auth")) {
                requestBuilder.header("Authorization", "Bearer $it")
            }
        }

        val requestWithHeaders: Request = requestBuilder.build()
        chain.proceed(requestWithHeaders)
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.4:8080/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun updateToken(newToken: String) {
        token = newToken
    }

    // produtos
    val produtoApi: ProdutoApi = retrofit.create(ProdutoApi::class.java)
    val modeloApi: ModeloApi = retrofit.create(ModeloApi::class.java)
    val tamanhoApi: TamanhoApi = retrofit.create(TamanhoApi::class.java)
    val corApi: CorApi = retrofit.create(CorApi::class.java)
    val categoriaApi: CategoriaApi = retrofit.create(CategoriaApi::class.java)
    val tipoApi: TipoApi = retrofit.create(TipoApi::class.java)
    val etpApi: EtpApi = retrofit.create(EtpApi::class.java)

    val authApi : AuthApi = retrofit.create(AuthApi::class.java)
    val lojaApi: LojaApi = retrofit.create(LojaApi::class.java)

    val vendaApi: VendaApi = retrofit.create(VendaApi::class.java)
}