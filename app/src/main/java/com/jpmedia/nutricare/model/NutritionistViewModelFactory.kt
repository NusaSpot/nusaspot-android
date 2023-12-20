package com.jpmedia.nutricare.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jpmedia.nutricare.ui.repository.NutritionistRepository


class NutritionistViewModelFactory(private val NutritionistRepository: NutritionistRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutritionistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NutritionistViewModel(NutritionistRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}