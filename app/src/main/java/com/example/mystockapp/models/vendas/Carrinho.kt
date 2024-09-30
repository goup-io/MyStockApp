package com.example.mystockapp.models.vendas

import com.example.mystockapp.models.produtos.ProdutoTable

data class Carrinho(
    val vendasInfo: VendasInfo,
    val itensCarrinho: List<ProdutoTable>
)

