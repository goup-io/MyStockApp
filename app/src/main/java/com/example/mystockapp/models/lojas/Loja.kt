package com.example.mystockapp.models.lojas

data class Loja(
    var id: Int = 0,
    var nome: String = "",
    var cnpj: String = "",
    var cep: String = "",
    var numero: Int = 0,
    var complemento: String = "",
    var endereco: Endereco = Endereco()
)

data class Endereco(
    var logradouro: String = "",
    var complemento: String = "",
    var bairro: String = "",
    var uf: String = "",
    var ibge: String = "",
    var gia: String = "",
    var ddd: String = "",
    var siafi: String = ""
)