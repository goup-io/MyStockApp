package com.example.mystockapp.modais.viewModels

import com.example.mystockapp.models.produtos.Produto
import com.example.mystockapp.models.produtos.ProdutoTable

interface ProdutoViewModel {
    var produtoSelecionado: ProdutoTable?  // Apenas o tipo padrão
    fun escolherProduto(produto: ProdutoTable)
    suspend fun pesquisarProdutoPorId(idEtp: Int): Produto?
}
