package com.example.mystockapp.models.vendas

import com.example.mystockapp.models.produtos.ProdutoTable

data class Carrinho(
    val vendaInfo: VendaInfo,
    val itensCarrinho: List<ProdutoTable>
)

