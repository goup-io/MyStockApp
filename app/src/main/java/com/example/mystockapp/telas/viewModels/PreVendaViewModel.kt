package com.example.mystockapp.telas.viewModels

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.produtoApi.ProdutoService
import com.example.mystockapp.modais.viewModels.AddProdEstoqueViewModel
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.models.vendas.Carrinho
import com.example.mystockapp.models.vendas.VendasInfo
import com.google.gson.Gson
import kotlinx.coroutines.launch

class PreVendaViewModel(private val idLoja: Int) : ViewModel() {
    var errorMessage by mutableStateOf<String?>(null)
        private set

    val gson = Gson();
    var produtos by mutableStateOf(listOf<ProdutoTable>())
        private set

    var carrinho by mutableStateOf(
        Carrinho(
            vendasInfo = VendasInfo(0.0, -1, -100),
            itensCarrinho = mutableListOf()
        )
    )
        private set

    fun addProduto(produto: ProdutoTable) {
        produto.quantidadeToAdd = produto.quantidadeToAdd.plus(1)
    }

    fun removerProduto(produto: ProdutoTable) {
        if (produto.quantidadeToAdd > 0){
            produto.quantidadeToAdd = (produto.quantidadeToAdd.minus(1))
        }
    }

    fun limparProdutos() {
        produtos = produtos
            .map { it.copy(quantidadeToAdd = 0) }
            .filter { it.quantidadeToAdd > 0 }
    }

    fun limparCarrinho(){
        carrinho = carrinho.copy(itensCarrinho = mutableListOf())
    }

    fun adicionar() {
        // Crie uma nova lista baseada na atual
        val novosItensCarrinho = carrinho.itensCarrinho.toMutableList()

        produtos.forEach { produto ->
            if (produto.quantidadeToAdd >= 1) {
                val itemCarrinho = novosItensCarrinho.find { it.id == produto.id }
                if (itemCarrinho != null) {
                    val updatedItem = itemCarrinho.copy(quantidadeToAdd = itemCarrinho.quantidadeToAdd + produto.quantidadeToAdd)
                    novosItensCarrinho[novosItensCarrinho.indexOf(itemCarrinho)] = updatedItem
                } else {
                    novosItensCarrinho.add(produto.copy(quantidadeToAdd = produto.quantidadeToAdd))
                }
            }
        }

        carrinho = carrinho.copy(itensCarrinho = novosItensCarrinho)

        limparProdutos()
        Log.d("Carrinho", "${gson.toJson(carrinho.itensCarrinho)}")
    }

    fun finalizarVenda(){


        limparCarrinho()
        limparProdutos()
    }



    suspend fun fetchProdutos(){
        viewModelScope.launch {
            val produtoService = ProdutoService(RetrofitInstance.produtoApi)
            try {
                produtos = produtoService.fetchProdutosTabela(idLoja = idLoja)
            } catch (e: ApiException) {
                errorMessage = "${e.message}"
            } catch (e: NetworkException) {
                errorMessage = "Network Error: ${e.message}"
            } catch (e: GeneralException) {
                errorMessage = "${e.message}"
            }
        }
    }

    class AddProdCarrinhoViewModelFactory(private val idLoja: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PreVendaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PreVendaViewModel(idLoja) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
