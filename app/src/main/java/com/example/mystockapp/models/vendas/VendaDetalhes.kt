package com.example.mystockapp.models.vendas

data class VendaDetalhes(
    var totalItens: Int,
    var subtotal1: Double,
    var valorDescontoProdutos: Double,
    var subtotal2: Double,
    var valorDescontoVenda: Double = 0.0,
    var porcentagemDesconto: Double = 0.0,
    var valorTotal: Double,
)
