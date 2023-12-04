package com.jpmedia.nusaspot.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jpmedia.nusaspot.api.DetectDataItem
import com.jpmedia.nusaspot.api.DetectResponse
import com.jpmedia.nusaspot.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetectRepository(private val apiService: UserApi) {
    fun getDetect(authorization: String): MutableLiveData<List<DetectDataItem>?> {
        val result = MutableLiveData<List<DetectDataItem>?>()

        apiService.getDetect(authorization).enqueue(object : Callback<DetectResponse> {
            override fun onResponse(call: Call<DetectResponse>, response: Response<DetectResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.data
                } else {
                    // Handle error
                    Log.e("DetectRepository", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DetectResponse>, t: Throwable) {
                Log.e("DetectRepository", "Request failed", t)
                // Handle failure
            }
        })

        return result
    }
}

