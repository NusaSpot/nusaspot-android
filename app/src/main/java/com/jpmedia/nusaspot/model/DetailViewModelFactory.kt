package com.jpmedia.nusaspot.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.jpmedia.nusaspot.ui.repository.DetailRepository

class DetailViewModelFactory(private val detailRepository: DetailRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetectDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetectDetailViewModel(detailRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
