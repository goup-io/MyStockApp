package com.example.mystockapp.models.produtos

data class ProdutoCreate (
    val codigo: String,
    val idCor: Int,
    val idModelo: Int,
    val nome: String,
    val valorCusto: Double,
    val valorRevenda: Double,
    val tamanho: Int,
    val itemPromocional:String,
    val idLoja: Int,
    val quantidade: Int
)
enum class ItemPromocional {
    SIM,
    NAO
}