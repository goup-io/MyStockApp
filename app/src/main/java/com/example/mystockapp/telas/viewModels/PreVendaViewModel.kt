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
import com.example.mystockapp.api.vendaApi.VendaService
import com.example.mystockapp.modais.viewModels.ProdutoViewModel
import com.example.mystockapp.models.produtos.Produto
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.models.vendas.Carrinho
import com.example.mystockapp.models.vendas.ProdutoVendaReq
import com.example.mystockapp.models.vendas.TipoVendasDataClass
import com.example.mystockapp.models.vendas.VendaDetalhes
import com.example.mystockapp.models.vendas.VendaInfo
import com.example.mystockapp.models.vendas.VendaPost
import com.google.gson.Gson
import kotlinx.coroutines.launch

class PreVendaViewModel(private val idLoja: Int) : ViewModel(), ProdutoViewModel {
    var errorMessage by mutableStateOf<String?>(null)
        private set

    val gson = Gson();
    var produtos by mutableStateOf(listOf<ProdutoTable>())
        private set

    var tipoVendas by mutableStateOf(listOf<TipoVendasDataClass>())
        private set

    var _produtoSelecionado = mutableStateOf<ProdutoTable?>(null)

    var abrirModalAdicionarMinimizado by mutableStateOf(false)

    var carrinho by mutableStateOf(
        Carrinho(
            vendaInfo = VendaInfo(0.0, -1, -100),
            itensCarrinho = mutableListOf()
        )
    )

    fun atualizarVendaInfo(tipoVendaId: Int, codigoVendedor: Int){
        carrinho = carrinho.copy(vendaInfo = carrinho.vendaInfo.copy(tipoVendaId = tipoVendaId, codigoVendedor = codigoVendedor))
    }

    var vendaDetalhes by mutableStateOf(
        VendaDetalhes(
            totalItens = 0,
            subtotal1 = 0.0,
            valorDescontoProdutos = 0.0,
            subtotal2 = 0.0,
            valorDescontoVenda = 0.0,
            valorTotal = 0.0
        )
    )
        private set

    override var produtoSelecionado: ProdutoTable?
        get() = _produtoSelecionado.value
        set(value) {
            _produtoSelecionado.value = value
        }

    override fun escolherProduto(produto: ProdutoTable){
        produtoSelecionado = produto
    }

    override suspend fun pesquisarProdutoPorId(idEtp: Int): Produto? {
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

        val novosItensCarrinho = carrinho.itensCarrinho.toMutableList()

        produtos.forEach { produto ->
            if (produto.quantidadeToAdd >= 1) {
                val itemCarrinho = novosItensCarrinho.find { it.id == produto.id }
                if (itemCarrinho != null) {
                    val updatedItem = itemCarrinho.copy(quantidadeToAdd = produto.quantidadeToAdd)
                    novosItensCarrinho[novosItensCarrinho.indexOf(itemCarrinho)] = updatedItem
                } else {
                    novosItensCarrinho.add(produto.copy(quantidadeToAdd = produto.quantidadeToAdd))
                }
            }
            if (verificarSeEstaCarrinho(produto) && produto.quantidadeToAdd == 0){
                val itemCarrinho = novosItensCarrinho.find { it.id == produto.id }
                novosItensCarrinho.remove(itemCarrinho)
            }
        }

        carrinho = carrinho.copy(itensCarrinho = novosItensCarrinho)
        atualizarVendaDetalhes()
    }

    fun atualizarVendaDetalhes() {
        vendaDetalhes = vendaDetalhes.copy(
            totalItens = calcularTotalItens(),
            subtotal1 = calcularSubtotal1(),
            valorDescontoProdutos = calcularValorDescontoProdutos(),
            subtotal2 = calcularSubtotal2(),
            valorDescontoVenda = calcularValorDescontoVenda(),
            valorTotal = calcularValorTotal()
        )
    }

    fun atualizarPosVenda(){
        vendaDetalhes = vendaDetalhes.copy(valorDescontoVenda = 0.0)
        vendaDetalhes = vendaDetalhes.copy(porcentagemDesconto = 0.0)
        carrinho = carrinho.copy(vendaInfo = carrinho.vendaInfo.copy(desconto = 0.0, codigoVendedor = 0, tipoVendaId = 0))
        atualizarVendaDetalhes()
    }
    private fun calcularValorTotal(): Double {
        return calcularSubtotal2() - calcularValorDescontoVenda()
    }

    private fun calcularValorDescontoVenda(): Double {
        val porcentagemDescAtual = vendaDetalhes.porcentagemDesconto
        val novoValorVenda = (porcentagemDescAtual / 100) * calcularSubtotal1()
        carrinho = carrinho.copy(vendaInfo = carrinho.vendaInfo.copy(desconto = novoValorVenda))
        return novoValorVenda;
    }

    private fun calcularSubtotal2(): Double {
        // subtotal 2 é valor unitário de cada produto - valor do desconto de cada um * quantidade
        return carrinho.itensCarrinho.sumOf { (it.preco - it.valorDesconto) * it.quantidadeToAdd }
    }

    private fun calcularValorDescontoProdutos(): Double {
        return carrinho.itensCarrinho.sumOf { it.valorDesconto * it.quantidadeToAdd }
    }

    private fun calcularSubtotal1(): Double {
        return carrinho.itensCarrinho.sumOf { it.preco * it.quantidadeToAdd }
    }

    private fun calcularTotalItens(): Int {
        return carrinho.itensCarrinho.sumOf { it.quantidadeToAdd }
    }

    fun adicionarDescontoVenda(valorDesconto: Double, porcentagemDesconto: Double){
        vendaDetalhes = vendaDetalhes.copy(valorDescontoVenda = valorDesconto)
        vendaDetalhes = vendaDetalhes.copy(porcentagemDesconto = porcentagemDesconto)
        carrinho = carrinho.copy(vendaInfo = carrinho.vendaInfo.copy(desconto = valorDesconto))
        atualizarVendaDetalhes()
    }

    fun adicionarDescontoProd(produtoParaAtualizar: ProdutoTable, valorDesconto: Double) {
        val novosItensCarrinho = carrinho.itensCarrinho.toMutableList()

        produtos.forEach { produto ->
            val itemCarrinho = novosItensCarrinho.find { it.id == produtoParaAtualizar.id }
            if (itemCarrinho != null) {
                val updatedItem = itemCarrinho.copy(valorDesconto = valorDesconto)
                novosItensCarrinho[novosItensCarrinho.indexOf(itemCarrinho)] = updatedItem
            } else {
                produto
            }
        }

        carrinho = carrinho.copy(itensCarrinho = novosItensCarrinho)

        atualizarVendaDetalhes()
    }

    suspend fun fetchProdutos() {
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

    suspend fun realizarVenda(){
        viewModelScope.launch {
            val vendaService = VendaService(RetrofitInstance.vendaApi)
            try {
                val produtosVendaReq = carrinho.itensCarrinho.map {
                    ProdutoVendaReq(
                        etpId = it.id,
                        quantidade = it.quantidadeToAdd,
                        desconto = it.valorDesconto * it.quantidadeToAdd
                    )
                }
                val vendaReq = VendaPost(
                    vendaReq = carrinho.vendaInfo,
                    produtosVendaReq = produtosVendaReq
                )
                val vendaRes = vendaService.createVenda(vendaReq)
                limparCarrinho()
                limparProdutos()
                atualizarPosVenda()
            } catch (e: ApiException) {
                errorMessage = "${e.message}"
            } catch (e: NetworkException) {
                errorMessage = "Network Error: ${e.message}"
            } catch (e: GeneralException) {
                errorMessage = "${e.message}"
            }
        }
    }

    suspend fun getTiposVenda() {

        try {
            val vendasService = VendaService(RetrofitInstance.vendaApi)
            val tipoVendaBuscados = vendasService.getTiposVenda()

            tipoVendas = tipoVendaBuscados
        } catch (e: ApiException) {
            errorMessage = "${e.message}"
        } catch (e: NetworkException) {
            errorMessage = "Network Error: ${e.message}"
        } catch (e: GeneralException) {
            errorMessage = "${e.message}"
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