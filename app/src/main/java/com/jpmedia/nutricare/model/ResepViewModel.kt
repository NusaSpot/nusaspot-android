package com.jpmedia.nutricare.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jpmedia.nutricare.api.Recipe
import com.jpmedia.nutricare.ui.repository.ResepRepository


class ResepViewModel(private val repository: ResepRepository) : ViewModel() {
    private val _recipeList = MutableLiveData<List<Recipe>?>()
    val recipeList: LiveData<List<Recipe>?> get() = _recipeList

    // Fungsi untuk mendapatkan resep tanpa pencarian
    fun fetchRecipes(authorization: String) {
        repository.getResep(authorization).observeForever {
            _recipeList.value = it
        }
    }

    // Fungsi untuk melakukan pencarian
    fun searchRecipes(authorization: String, searchTerm: String) {
        repository.searchRecipes(authorization, searchTerm).observeForever {
            _recipeList.value = it
        }
    }
}
