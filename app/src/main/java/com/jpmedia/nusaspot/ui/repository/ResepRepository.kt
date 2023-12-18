package com.jpmedia.nusaspot.ui.repository
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jpmedia.nusaspot.api.FinishResponse
import com.jpmedia.nusaspot.api.Recipe
import com.jpmedia.nusaspot.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResepRepository(private val apiService: UserApi) {

    fun getResep(authorization: String): MutableLiveData<List<Recipe>?> {
        val result = MutableLiveData<List<Recipe>?>()
        apiService.getResep(authorization).enqueue(object : Callback<FinishResponse> {
            override fun onResponse(call: Call<FinishResponse>, response: Response<FinishResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.data
                } else {
                    Log.e("RecipeRepository", "Unsuccessful response: ${response.code()}")
                    result.value = null
                }
            }

            override fun onFailure(call: Call<FinishResponse>, t: Throwable) {
                Log.e("RecipeRepository", "Request failed", t)
                result.value = null
            }
        })
        return result
    }

    fun searchRecipes(authorization: String, searchTerm: String): MutableLiveData<List<Recipe>?> {
        val result = MutableLiveData<List<Recipe>?>()
        apiService.getSearchResep(searchTerm, authorization).enqueue(object : Callback<FinishResponse> {
            override fun onResponse(call: Call<FinishResponse>, response: Response<FinishResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.data
                } else {
                    Log.e("RecipeRepository", "Unsuccessful response: ${response.code()}")
                    result.value = null
                }
            }

            override fun onFailure(call: Call<FinishResponse>, t: Throwable) {
                Log.e("RecipeRepository", "Request failed", t)
                result.value = null
            }
        })
        return result
    }
}


