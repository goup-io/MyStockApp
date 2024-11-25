package com.example.mystockapp.models.produtos

data class EtpRes (
    val id: Int,
    val codigo: String,
    val produto: String,
    val modelo: String,
    val cor: String,
    val tamanho: Int,
    val quantidade: Int,
    val valorCusto: Double,
    val valorRevenda: Double,
    val itemPromocional: String
)