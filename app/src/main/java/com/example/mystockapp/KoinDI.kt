package com.example.mystockapp


import org.koin.dsl.module

// módulo de injeção de dependências com quantos objetos forem necessários
// todos os objetos aqui serão gerenciados pelo Koin
val moduloUsuario = module {

    /*
    single -> indica que o objeto será instanciado uma única vez
              Ou seja, todos os lugares que pedirem um objeto do tipo SessaoUsuario
              receberão a MESMA instância
     */
    single<SessaoUsuario> {
        // estamos dizendo para o Koin como criar um objeto do tipo SessaoUsuario para o primeiro que pedir
        // da 2a vez em diante, usará a instância criada anteriormente
        SessaoUsuario()
    }



}