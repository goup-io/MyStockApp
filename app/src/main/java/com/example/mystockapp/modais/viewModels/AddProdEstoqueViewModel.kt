package com.example.mystockapp.modais.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.produtoApi.ProdutoService
import com.example.mystockapp.models.produtos.AdicionarEstoqueReq
import com.example.mystockapp.models.produtos.ProdutoTable
import com.google.gson.Gson
import kotlinx.coroutines.launch

class AddProdEstoqueViewModel(private val idLoja: Int) : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)
        public set

    var produtos by mutableStateOf(listOf<ProdutoTable>())
        private set

    fun fetchProdutos() {
        viewModelScope.launch {
            val produtoService = ProdutoService(RetrofitInstance.produtoApi)
            try {
                    produtos = produtoService.fetchProdutosTabela(1)
            } catch (e: ApiException) {
                    errorMessage = "${e.message}"
                } catch (e: NetworkException) {
                    errorMessage = "Network Error: ${e.message}"
                } catch (e: GeneralException) {
                    errorMessage = "${e.message}"
                }
        }


    }

    fun addProduto(produto: ProdutoTable) {
        produto.quantidadeToAdd = (produto.quantidadeToAdd.plus(1))
    }


    fun removerProduto(produto: ProdutoTable) {
        if (produto.quantidadeToAdd > 0){
            produto.quantidadeToAdd = (produto.quantidadeToAdd.minus(1))
        }
    }

    fun limparProdutos() {
        produtos = produtos.map { produto ->
            produto.copy(quantidadeToAdd = 0)
        }
    }

    suspend fun handleAdicionarProdutos() {
        val produtoService = ProdutoService(RetrofitInstance.produtoApi)
        val gson = Gson()

        Log.d("AddProdEstoqueViewModel", "produtos: ${gson.toJson(produtos)}")

        val estoqueReqList = produtos
            .filter { produto -> produto.quantidadeToAdd > 0 }
            .map { produto ->
                AdicionarEstoqueReq(
                    idEtp = produto.id,
                    quantidade = produto.quantidadeToAdd
                )
            }
        Log.d("AddProdEstoqueViewModel", "estoqueReqList: ${gson.toJson(estoqueReqList)}")
        try {
            produtoService.addProdutosEstoque(estoqueReqList, idLoja)
        } catch (e: ApiException) {
            throw e
        } catch (e: NetworkException) {
            throw e
        } catch (e: GeneralException) {
            throw e
        }

        limparProdutos();
    }
}

class AddProdEstoqueViewModelFactory(private val idLoja: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddProdEstoqueViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddProdEstoqueViewModel(idLoja) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}