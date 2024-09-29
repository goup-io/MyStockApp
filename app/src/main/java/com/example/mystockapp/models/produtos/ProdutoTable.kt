package com.example.mystockapp.models.produtos

import com.example.mystockapp.models.vendas.ListaItensModal

data class ProdutoTable(
    val id: Int,
    val nome: String,
    val modelo: String,
    val preco: Double,
    val tamanho: Int,
    val cor: String,
    var quantidade: Int,
    var quantidadeToAdd: Int = 0
) : ListaItensModal