package com.jpmedia.nusaspot.ui.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jpmedia.nusaspot.api.DetailData
import com.jpmedia.nusaspot.api.DetectDetailResponse
import com.jpmedia.nusaspot.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailRepository(private val apiService: UserApi) {
    fun getDetail(detectId: String, authorization: String): MutableLiveData<List<DetailData>?> {
        val result = MutableLiveData<List<DetailData>?>()
        apiService.getDetailDetect(detectId, authorization).enqueue(object : Callback<DetectDetailResponse> {
            override fun onResponse(call: Call<DetectDetailResponse>, response: Response<DetectDetailResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.data
                } else {
                    // Handle error
                    Log.e("DetectDetailRepository", "Unsuccessful response: ${response.code()}, Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetectDetailResponse>, t: Throwable) {
                Log.e("DetectDetailRepository", "Request failed", t)
                // Handle failure
            }
        })
        return result
    }
}