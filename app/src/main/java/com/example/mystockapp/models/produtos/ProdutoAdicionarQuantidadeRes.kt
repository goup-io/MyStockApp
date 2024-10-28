package com.example.mystockapp.models.produtos

data class ProdutoAdicionarQuantidadeRes(
    val id: Int,
    val codigo: String,
    val nome: String,
    val modelo: String,
    val tamanho: Int,
    val cor: String,
    val preco: Double,
    val loja: String,
    val itemPromocional: String,
    val quantidade: Int,
    val idProduto: Int
)
