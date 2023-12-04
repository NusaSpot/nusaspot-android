package com.jpmedia.nusaspot.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jpmedia.nusaspot.repository.DetectRepository
import com.jpmedia.nusaspot.model.DetectViewModel

class DetectViewModelFactory(private val detectRepository: DetectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetectViewModel(detectRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

