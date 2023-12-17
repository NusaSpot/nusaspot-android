package com.jpmedia.nusaspot.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jpmedia.nusaspot.api.Recipe
import com.jpmedia.nusaspot.ui.repository.ResepRepository


class ResepViewModel(private val repository: ResepRepository) : ViewModel() {
    private val _recipeList = MutableLiveData<List<Recipe>?>()
    val recipeList: LiveData<List<Recipe>?> get() = _recipeList

    fun fetchRecipes(authorization: String) {
        repository.getResep(authorization).observeForever {
            _recipeList.value = it
        }
    }
}
