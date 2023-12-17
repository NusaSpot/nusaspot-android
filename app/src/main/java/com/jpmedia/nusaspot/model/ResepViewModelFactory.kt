package com.jpmedia.nusaspot.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jpmedia.nusaspot.ui.repository.ResepRepository

class ResepViewModelFactory(private val ResepRepository: ResepRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResepViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResepViewModel(ResepRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
