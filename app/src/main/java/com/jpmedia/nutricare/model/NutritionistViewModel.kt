package com.jpmedia.nutricare.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jpmedia.nutricare.api.NutritionistData
import com.jpmedia.nutricare.ui.repository.NutritionistRepository


class NutritionistViewModel(private val repository: NutritionistRepository) : ViewModel() {
    private val _nutritionistList = MutableLiveData<List<NutritionistData>?>()
    val recipeList: LiveData<List<NutritionistData>?> get() = _nutritionistList
    fun getNutritionist(authorization: String) {
        repository.getNutritionist(authorization).observeForever {
            _nutritionistList.value = it
        }
    }

    fun searchNutritionists(authorization: String, searchTerm: String) {
        repository.searchNutritionists(authorization, searchTerm).observeForever {
            _nutritionistList.value = it
        }
    }
}