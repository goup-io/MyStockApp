package com.example.mystockapp.models.produtos

data class ProdutoEdit (
    val codigo: String,
    val nome: String,
    val valorCusto: Double,
    val valorRevenda: Double,
    val itemPromocional: String,
    val quantidade: Int
)