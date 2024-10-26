package com.example.mystockapp.modais.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystockapp.R
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.produtoApi.ProdutoService
import com.example.mystockapp.models.produtos.AdicionarEstoqueReq
import com.example.mystockapp.models.produtos.Produto
import com.example.mystockapp.models.produtos.ProdutoTable
import com.google.gson.Gson
import kotlinx.coroutines.launch

class AddProdEstoqueViewModel(private val idLoja: Int) : ViewModel(), ProdutoViewModel {

    val gson = Gson();

    var errorMessage by mutableStateOf<String?>(null)
        public set

    var showConfirmDialog by mutableStateOf(false)

    var showSucessoDialog by mutableStateOf(false)

    var executarFuncao by mutableStateOf(false)

    var sucessoDialogTitulo by mutableStateOf("")

    var imgCasoDeErro by mutableStateOf<Int?>(null)

    var produtos by mutableStateOf(listOf<ProdutoTable>())
        private set

    var _produtoSelecionado = mutableStateOf<ProdutoTable?>(null)

    var etpId by mutableStateOf(0)
    var quantidade by mutableStateOf(0)

    fun fetchProdutos() {
        viewModelScope.launch {
            val produtoService = ProdutoService(RetrofitInstance.produtoApi)
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

    fun adicionarNoEstoque(quantidade:Int){
        val etpId = produtoSelecionado?.id ?: return

        if (produtoSelecionado == null) {
            throw IllegalArgumentException("produtoSelecionado não pode ser nulo")
        }

        this.etpId = etpId
        this.quantidade = quantidade

        showConfirmDialog = true
    }

    suspend fun adicionar(){
        if (executarFuncao){
            val produtosParaAdd = listOf(AdicionarEstoqueReq(etpId, quantidade))

            val produtoService = ProdutoService(RetrofitInstance.produtoApi)

            if (idLoja == null) {
                throw IllegalArgumentException("idLoja não pode ser nulo")
            }
            try {
                Log.d("AddProdEstoqueViewModel", "produtosParaAdd: ${gson.toJson(produtosParaAdd)}")
                produtoService.addProdutosEstoque(produtosParaAdd, idLoja)
                sucessoDialogTitulo = "Produto adicionado com sucesso!"
                imgCasoDeErro = R.mipmap.ic_sucesso
                showSucessoDialog = true
            } catch (e: ApiException) {
                if (quantidade <=0 ){
                    sucessoDialogTitulo = "Quantidade de produtos precisa ser maior que 0"
                } else {
                    sucessoDialogTitulo = "Erro ao adicionar produto"
                }
                imgCasoDeErro = R.mipmap.ic_excluir
                showSucessoDialog = true
            } catch (e: NetworkException) {
                imgCasoDeErro = R.mipmap.ic_excluir
                sucessoDialogTitulo = "Erro ao adicionar produto"
                showSucessoDialog = true
            } catch (e: GeneralException) {
                imgCasoDeErro = R.mipmap.ic_excluir
                sucessoDialogTitulo = "Erro ao adicionar produto"
                showSucessoDialog = true
            }
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
}