package com.example.mystockapp.models.vendas

// equivalente ao produtoVendaReq

data class ItemCarrinho (
    var etpId: Int,
    var quantidade: Int,
    var desconto: Double,
)
