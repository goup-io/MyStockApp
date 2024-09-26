package com.example.mystockapp.models.produtos

data class ProdutoEditarGet(
    var id: Int = 0,
    var codigo: String = "",
    var nome: String = "",
    var modelo: String = "",
    var tamanho: Int = 0,
    var cor: String = "",
    var precoCusto: Double = 0.0,
    var precoRevenda: Double = 0.0,
    var idLoja: Int = 0,
    var idProduto: Int = 0,
    var itemPromocional: String = "",
    var quantidade: Int = 0
)