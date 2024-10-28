package com.example.mystockapp.api.produtoApi

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EtpViewModelFactory(private val etpApi: EtpApi, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EtpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            Log.d("EtpViewModelFactory", "EtpViewModelFactory: ${EtpViewModel(etpApi, context)}")
            return EtpViewModel(etpApi, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}