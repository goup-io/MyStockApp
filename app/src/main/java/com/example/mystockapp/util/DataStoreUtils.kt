package com.example.mystockapp.util

import android.content.Context
import android.util.Log
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mystockapp.SessaoUsuario
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.sessaoUsuario: DataStore<Preferences>
        by preferencesDataStore("sessaoUsuario")

object DataStoreUtils {

    var login = ""
    var senha = ""
    var token = ""

    fun salvarUsuarioOffline(login:String, senha:String, token:String, context: Context){
        GlobalScope.launch {
            val dataStoreKey = stringPreferencesKey("login")
            val dataStoreKey2 = stringPreferencesKey("senha")
            val dataStoreKey3 = stringPreferencesKey("token")
            context.sessaoUsuario.edit {
                it[dataStoreKey] = login
                it[dataStoreKey2] = senha
                it[dataStoreKey3] = token
            }
        }
    }

    suspend fun recuperarUsuarioOffline(context: Context): Map<String, String> {
        val preferences = context.sessaoUsuario.data.first()
        return mapOf(
            "login" to (preferences[stringPreferencesKey("login")] ?: ""),
            "senha" to (preferences[stringPreferencesKey("senha")] ?: ""),
            "token" to (preferences[stringPreferencesKey("token")] ?: "")
        )
    }

    fun excluirUsuarioOffline(context: Context){
        GlobalScope.launch {
            context.sessaoUsuario.edit {
                it.clear()
            }
        }
    }
}