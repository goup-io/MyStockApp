package com.example.mystockapp.models.vendas

data class VendaPost(
    val vendaReq : VendaInfo,
    val produtosVendaReq : List<ProdutoVendaReq>
)
