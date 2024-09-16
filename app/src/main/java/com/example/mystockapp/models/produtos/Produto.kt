package com.example.mystockapp.models.produtos

data class Produto (
    val codigo : String,
    val nome : String,
    val cor : String,
    val preco : Double,
    val modelo : String,
    val quantidade : Int,
    val tamanho : Int
)