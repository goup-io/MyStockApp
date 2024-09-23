package com.example.mystockapp.api

import com.example.mystockapp.api.produtoApi.ProdutoApi
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

    val headerInterceptor = Interceptor { chain ->
        val originalRequest: Request = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoZW50aWNhdGlvbiIsInN1YiI6InRlc3RlIiwiaWQiOjEsImV4cCI6MTcyNjUxODQzNn0.vw11Kg8kvdY_YFEXvQIvQT2cOIsWFaBg3hBNRfIxl3A"
        if (!originalRequest.url.toString().contains("auth")) {
            requestBuilder.header("Authorization", "Bearer $token")
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
            .baseUrl("http://192.168.0.106:8080/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val produtoApi: ProdutoApi = retrofit.create(ProdutoApi::class.java)
}