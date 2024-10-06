package com.example.mystockapp.models.vendas

data class VendaRes(
    val id: Int,
    val dataHora: String,
    val desconto: Double,
    val valorTotal: Double,
    val status: String,
    val tipoVenda: String,
    val vendedor: String
)
