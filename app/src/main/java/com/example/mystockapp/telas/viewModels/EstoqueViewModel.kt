package com.example.mystockapp.telas.viewModels

import android.util.Log
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
import com.example.mystockapp.modais.viewModels.ProdutoViewModel
import com.example.mystockapp.models.produtos.Produto
import com.example.mystockapp.models.produtos.ProdutoTable
import com.google.gson.Gson
import kotlinx.coroutines.launch

class EstoqueViewModel(private val idLoja: Int) : ViewModel(), ProdutoViewModel {

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var produtos by mutableStateOf(listOf<ProdutoTable>())
        private set

    var isSearching by mutableStateOf(false)
        private set

    val gson = Gson()

    private val produtoService = ProdutoService(RetrofitInstance.produtoApi)

    var atualizarEstoque by mutableStateOf(false)

    fun atualizarEstoque(){
        fetchProdutos()
        atualizarEstoque = false
        Log.d("Estoque", "Estoque atualizado")
    }

    // Função para buscar todos os produtos inicialmente
    fun fetchProdutos() {
        viewModelScope.launch {
            try {
                produtos = produtoService.fetchProdutosTabela(idLoja)
            } catch (e: ApiException) {
                errorMessage = "${e.message}"
            } catch (e: NetworkException) {
                errorMessage = "Network Error: ${e.message}"
            } catch (e: GeneralException) {
                errorMessage = "${e.message}"
            }
        }
    }

    // Função de busca simples por nome ou código
    fun buscarProdutos(query: String) {
        viewModelScope.launch {
            isSearching = true
            try {
                val resultadoBusca = produtoService.buscarProdutos(query, idLoja)
                produtos = resultadoBusca
            } catch (e: ApiException) {
                errorMessage = "Erro na API: ${e.message}"
            } catch (e: NetworkException) {
                errorMessage = "Erro de conexão: ${e.message}"
            } catch (e: GeneralException) {
                errorMessage = "${e.message}"
            } finally {
                isSearching = false
            }
        }
    }

    // Função de busca por filtros (modelo, tamanho, cor, preço)
    fun buscarPorFiltros(
        modelo: String?,
        tamanho: Int?,
        cor: String?,
        precoMin: Double?,
        precoMax: Double?
    ) {
        viewModelScope.launch {
            try {
                // Variáveis opcionais que vamos usar para passar os filtros
                var modeloParam: String? = null
                var tamanhoParam: Int? = null
                var corParam: String? = null
                var precoMinParam: Double? = null
                var precoMaxParam: Double? = null

                // Verificar se os parâmetros não estão vazios antes de atribuí-los
                if (!modelo.isNullOrBlank()) {
                    modeloParam = modelo
                }
                if (tamanho != null && tamanho > 0) {
                    tamanhoParam = tamanho
                }
                if (!cor.isNullOrBlank()) {
                    corParam = cor
                }
                if (precoMin != null && precoMin > 0) {
                    precoMinParam = precoMin
                }
                if (precoMax != null && precoMax > 0) {
                    precoMaxParam = precoMax
                }

                // Agora fazer a requisição com os parâmetros que têm valores válidos
                val response = produtoService.buscarProdutosPorFiltros(
                    idLoja = idLoja,
                    modelo = modeloParam,
                    tamanho = tamanhoParam,
                    cor = corParam,
                    precoMin = precoMinParam,
                    precoMax = precoMaxParam
                )
                produtos = response

            } catch (e: Exception) {
                errorMessage = "Erro: ${e.message}"
            }
        }
    }




    class EstoqueViewModelFactory(private val idLoja: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EstoqueViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EstoqueViewModel(idLoja) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    var _produtoSelecionado = mutableStateOf<ProdutoTable?>(null)

    override var produtoSelecionado: ProdutoTable?
        get() = _produtoSelecionado.value
        set(value) {
            _produtoSelecionado.value = value
        }


    override fun escolherProduto(produto: ProdutoTable) {
        produtoSelecionado = produto
    }

    override suspend fun pesquisarProdutoPorId(idEtp: Int): Produto? {
        Log.d("Produto", "Pesquisando produto por id: $idEtp")

        return try {
            val produtoService = ProdutoService(RetrofitInstance.produtoApi)
            val produtoBuscado = produtoService.fetchEtpPorId(idEtp)
            Log.d("Produto", "Produto encontrado: ${gson.toJson(produtoBuscado)}")
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
}



