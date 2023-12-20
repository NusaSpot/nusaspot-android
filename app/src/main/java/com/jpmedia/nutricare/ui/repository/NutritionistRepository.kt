package com.jpmedia.nutricare.ui.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jpmedia.nutricare.api.NutritionistData
import com.jpmedia.nutricare.api.NutritionistResponse
import com.jpmedia.nutricare.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NutritionistRepository(private val apiService: UserApi) {

    fun getNutritionist(authorization: String): MutableLiveData<List<NutritionistData>?> {
        val result = MutableLiveData<List<NutritionistData>?>()
        apiService.getNutritionist(authorization).enqueue(object : Callback<NutritionistResponse> {
            override fun onResponse(
                call: Call<NutritionistResponse>,
                response: Response<NutritionistResponse>
            ) {
                if (response.isSuccessful) {
                    result.value = response.body()?.data
                } else {
                    Log.e("RecipeRepository", "Unsuccessful response: ${response.code()}")
                    result.value = null
                }
            }

            override fun onFailure(call: Call<NutritionistResponse>, t: Throwable) {
                Log.e("RecipeRepository", "Request failed", t)
                result.value = null
            }
        })
        return result
    }

    fun searchNutritionists(authorization: String, searchTerm: String): MutableLiveData<List<NutritionistData>?> {
        val result = MutableLiveData<List<NutritionistData>?>()
        apiService.serachNutritionist(searchTerm, authorization).enqueue(object : Callback<NutritionistResponse> {
            override fun onResponse(call: Call<NutritionistResponse>, response: Response<NutritionistResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.data
                } else {
                    Log.e("NutritionistRepository", "Unsuccessful response: ${response.code()}")
                    result.value = null
                }
            }

            override fun onFailure(call: Call<NutritionistResponse>, t: Throwable) {
                Log.e("NutritionistRepository", "Request failed", t)
                result.value = null
            }
        })
        return result
    }
}