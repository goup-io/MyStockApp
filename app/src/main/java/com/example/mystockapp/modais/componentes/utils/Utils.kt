package com.example.mystockapp.modais.componentes.utils

import android.util.Log

fun formatarPreco(input: String): String {
    var numeros = input.replace(Regex("[^0-9,]"), "") // Remove tudo, exceto números e vírgulas
    if (numeros.count { it == ',' } > 1) {
        numeros = numeros.substring(0, numeros.length - 1) // Limita a uma vírgula
    }

    val parts = numeros.split(",") // Divide em partes
    var integerPart = parts[0]
    var decimalPart = if (parts.size > 1) parts[1] else ""

    if (decimalPart.length > 2) { // Limita os decimais
        decimalPart = decimalPart.substring(0, 2)
    }

    // Adiciona separadores de milhar
    integerPart = integerPart.reversed().chunked(3).joinToString(".").reversed()

    // Formata o número
    return if (decimalPart.isNotEmpty() || input.endsWith(",")) {
        "$integerPart,$decimalPart"
    } else {
        integerPart
    }
}

fun desformatarPreco(input: String): Double {
    val numeros = input.replace(Regex("[^0-9,]"), "")
    return if (numeros.isEmpty()) {
        0.0
    } else {
        numeros.replace(",", ".").toDouble()
    }
}

fun formatarPrecoParaTexto(Double: Double): String {
    val stringFormatada = String.format("%.2f", Double)
    Log.d("formatarPrecoParaTexto", "stringFormatada: $stringFormatada")
    return stringFormatada
}