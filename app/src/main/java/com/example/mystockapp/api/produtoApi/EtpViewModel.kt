package com.example.mystockapp.api.produtoApi

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystockapp.models.produtos.EtpRes
import kotlinx.coroutines.launch

class EtpViewModel(private val etpApi: EtpApi, private val context: Context) : ViewModel() {

    val etp = MutableLiveData<EtpRes?>()

    fun buscarEtpPorCodigo(codigo: String) {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences("MyStockPrefs", Context.MODE_PRIVATE)
            val idLoja = sharedPreferences.getInt("idLoja", -1)
            if (idLoja != -1) {
                try {
                    val response = etpApi.buscarPorCodigo(codigo, idLoja)
                    etp.postValue(response)
                } catch (e: Exception) {
                    // Handle the exception, e.g., log it or show a message to the user
                    etp.postValue(null)
                }
            }
        }
    }

    private val _contextoBusca = mutableStateOf("")
    val contextoBusca: State<String> get() = _contextoBusca

    fun setContextoBusca(value: String) {
        _contextoBusca.value = value
    }
}