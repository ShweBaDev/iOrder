package com.galaxysoftware.istockenterpriseiorder.views.ui.checkout

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CheckoutViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            return CheckoutViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}