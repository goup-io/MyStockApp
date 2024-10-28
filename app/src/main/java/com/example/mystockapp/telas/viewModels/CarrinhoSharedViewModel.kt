package com.example.mystockapp.telas.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.produtoApi.ProdutoService
import com.example.mystockapp.models.produtos.Produto
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.models.vendas.Carrinho
import com.example.mystockapp.models.vendas.VendaInfo

object CarrinhoSharedViewModel {
    var produtos by mutableStateOf(listOf<ProdutoTable>())
    private var _produtoSelecionado = mutableStateOf<ProdutoTable?>(null)
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var carrinho by mutableStateOf(
        Carrinho(
            vendaInfo = VendaInfo(0.0, -1, -100),
            itensCarrinho = mutableListOf()
        )
    )

    var produtoSelecionado: ProdutoTable?
        get() = _produtoSelecionado.value
        set(value) {
            _produtoSelecionado.value = value
        }

    fun escolherProduto(produto: ProdutoTable) {
        produtoSelecionado = produto
    }

    suspend fun pesquisarProdutoPorId(idEtp: Int): Produto? {
        return try {
            val produtoService = ProdutoService(RetrofitInstance.produtoApi)
            val produtoBuscado = produtoService.fetchEtpPorId(idEtp)
            produtoBuscado
        } catch (e: ApiException) {
            errorMessage = "${e.message}"
            null
        } catch (e: NetworkException) {
            errorMessage = "Network Error: ${e.message}"
            null
        } catch (e: GeneralException) {
            errorMessage = "${e.message}"
            null
        }
    }

    fun atualizarQuantidadeProduto(quantidade: Int){
        produtoSelecionado = produtoSelecionado?.copy(quantidadeToAdd = quantidade)
        produtos = produtos.map {
            if (it.id == produtoSelecionado!!.id) {
                it.copy(quantidadeToAdd = quantidade)
            } else {
                it
            }
        }
    }

    fun limparProdutos() {
        produtos = produtos
            .map { it.copy(quantidadeToAdd = 0, valorDesconto = 0.0) }
            .filter { it.quantidadeToAdd > 0 }
    }

    fun limparCarrinho(){
        carrinho = carrinho.copy(itensCarrinho = mutableListOf())
    }

    fun verificarSeEstaCarrinho(produto: ProdutoTable):Boolean {
        val itemEncontrado = carrinho.itensCarrinho.find { it.id == produto.id }
        return itemEncontrado != null;
    }

    fun adicionar(quantidade: Int) {
        atualizarQuantidadeProduto(quantidade)
        Log.d("CarrinhoSharedViewModel", "Adicionando produto ao carrinho: ${produtoSelecionado}")
        val novosItensCarrinho = carrinho.itensCarrinho.toMutableList()
        Log.d("CarrinhoSharedViewModel", "Itens carrinho: ${novosItensCarrinho}")
        Log.d("CarrinhoSharedViewModel", "Produtos em estoque: ${produtos}")
        produtos.forEach { produto ->
            Log.d("CarrinhoSharedViewModel", "Produto: ${produto}")
            if (produto.quantidadeToAdd >= 1) {
                Log.d("CarrinhoSharedViewModel", "Produto adicionado ao carrinho: ${produto}")
                val itemCarrinho = novosItensCarrinho.find { it.id == produto.id }
                if (itemCarrinho != null) {
                    val updatedItem = itemCarrinho.copy(quantidadeToAdd = produto.quantidadeToAdd)
                    novosItensCarrinho[novosItensCarrinho.indexOf(itemCarrinho)] = updatedItem
                    Log.d("CarrinhoSharedViewModel", "Produto atualizado no carrinho: ${produto}")
                } else {
                    Log.d("CarrinhoSharedViewModel", "Produto não encontrado no carrinho: ${produto}")
                    novosItensCarrinho.add(produto.copy(quantidadeToAdd = produto.quantidadeToAdd))
                }
            }
            if (verificarSeEstaCarrinho(produto) && produto.quantidadeToAdd == 0){
                val itemCarrinho = novosItensCarrinho.find { it.id == produto.id }
                Log.d("CarrinhoSharedViewModel", "Produto removido do carrinho: ${produto}")
                novosItensCarrinho.remove(itemCarrinho)
            }
        }

        carrinho = carrinho.copy(itensCarrinho = novosItensCarrinho)
    }

    suspend fun fetchProdutos(idLoja: Int) {
        try {
            val produtoService = ProdutoService(RetrofitInstance.produtoApi)
            val produtosBuscados = produtoService.fetchProdutosTabela(idLoja = idLoja)

            produtos = produtosBuscados.map { produtoBuscado ->
                val produtoExistente = produtos.find { it.id == produtoBuscado.id }
                if (produtoExistente != null) {
                    Log.d("FetchProdutos", "Produto encontrado: ${produtoExistente.id}, quantidadeToAdd: ${produtoExistente.quantidadeToAdd}")
                } else {
                    Log.d("FetchProdutos", "Produto não encontrado: ${produtoBuscado.id}")
                }
                produtoBuscado.copy(quantidadeToAdd = produtoExistente?.quantidadeToAdd ?: 0)
            }
        } catch (e: ApiException) {
            errorMessage = "${e.message}"
        } catch (e: NetworkException) {
            errorMessage = "Network Error: ${e.message}"
        } catch (e: GeneralException) {
            errorMessage = "${e.message}"
        }
    }
}