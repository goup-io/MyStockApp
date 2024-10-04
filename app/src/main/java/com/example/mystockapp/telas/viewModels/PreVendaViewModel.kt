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
import com.example.mystockapp.models.produtos.ProdutoTable
import com.example.mystockapp.models.vendas.Carrinho
import com.example.mystockapp.models.vendas.VendaDetalhes
import com.example.mystockapp.models.vendas.VendaInfo
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
            vendaInfo = VendaInfo(0.0, -1, -100),
            itensCarrinho = mutableListOf()
        )
    )
        private set

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

    private fun calcularValorTotal(): Double {
        return calcularSubtotal2() - calcularValorDescontoVenda()
    }

    private fun calcularValorDescontoVenda(): Double {
        return vendaDetalhes.valorDescontoVenda
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
        Log.d("Desconto", "FUI CHAMADO COM O VALOR: $valorDesconto")
        vendaDetalhes = vendaDetalhes.copy(valorDescontoVenda = valorDesconto)
        vendaDetalhes = vendaDetalhes.copy(porcentagemDesconto = porcentagemDesconto)
        carrinho = carrinho.copy(vendaInfo = carrinho.vendaInfo.copy(desconto = valorDesconto))
        atualizarVendaDetalhes()
    }

    fun adicionarDescontoProd(ProdutoTable: ProdutoTable, valorDesconto: Double){
//        val itemCarrinho = carrinho.itensCarrinho.find { it.id == ProdutoTable.id }
//        if (itemCarrinho != null) {
//            val updatedItem = itemCarrinho.copy(valorDesconto = valorDesconto)
//            carrinho.itensCarrinho[carrinho.itensCarrinho.indexOf(itemCarrinho)] = updatedItem
//        }
//        atualizarVendaDetalhes()
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
