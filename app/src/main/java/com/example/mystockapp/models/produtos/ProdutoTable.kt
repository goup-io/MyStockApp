package com.example.mystockapp.models.produtos

data class ProdutoTable(
    val nome: String,
    val modelo: String,
    val preco: Double,
    val tamanho: Int,
    val cor: String,
    val quantidade: String
)