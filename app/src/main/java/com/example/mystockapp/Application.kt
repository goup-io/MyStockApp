package com.example.mystockapp


import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import com.example.mystockapp.moduloUsuario

/*
Classe que inicializa o Koin.
É necessário que ela seja declarada no AndroidManifest.xml
 */
class Aplicacao: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@Aplicacao)

            // aqui estamos dizendo para o Koin usar o módulo que criamos
            modules(moduloUsuario)
            // modules(moduloXYZ)
            // poderiamos ter mais de um módulo, se necessário
        }
    }
}