package com.example.mystockapp.models.vendas

data class ProdutoVendaReq(
    val etpId: Int,
    val quantidade: Int,
    val desconto: Double
)
