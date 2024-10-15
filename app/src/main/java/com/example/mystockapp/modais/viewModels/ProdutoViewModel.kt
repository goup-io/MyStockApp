package com.example.mystockapp.modais.viewModels

import com.example.mystockapp.models.produtos.ProdutoTable

interface ProdutoViewModel {
    fun escolherProduto(produto: ProdutoTable)
}