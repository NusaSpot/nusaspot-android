package com.jpmedia.nusaspot.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jpmedia.nusaspot.api.NutritionistData
import com.jpmedia.nusaspot.ui.repository.NutritionistRepository


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