package com.example.mystockapp.models.vendas

data class Carrinho(
    var vendasInfo: VendasInfo,
    var itensCarrinho: MutableList<ItemCarrinho> = mutableListOf(),
) : ListaItensModal
