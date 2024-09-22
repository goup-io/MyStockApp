package com.example.mystockapp.api.authApi

import com.example.mystockapp.models.auth.LoginRequest
import com.example.mystockapp.models.auth.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}