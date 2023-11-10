package com.jpmedia.nusaspot.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class HomeViewModel : ViewModel() {

    private val _userData = MutableLiveData<UserResponse?>()
    val userData: LiveData<UserResponse?> get() = _userData

    fun loadUserData(token: String) {
        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        val bearerToken = "Bearer $token"

        retro.getUsers(bearerToken).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val usersResponse = response.body()
                    _userData.value = usersResponse
                } else {
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

            }
        })
    }
}


