package com.jpmedia.nusaspot.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {
    @POST("api/login")
    fun  login(
        @Body userRequest: UserRequest
    ): retrofit2.Call<UserResponse>

    @GET("api/login-status")
    fun getUsers(@Header("Authorization") authorization: String): retrofit2.Call<UserResponse>

    @POST("api/register")
    fun register(
        @Body userRequest: UserRequest
    ):retrofit2.Call<UserResponse>
    @POST("api/request-otp")
    fun requestOtp(
        @Body userRequest: UserRequest
    ):retrofit2.Call<UserResponse>

    @POST("api/verify-otp")
    fun postOtp(
        @Body userRequest: UserRequest
    ):retrofit2.Call<OtpResponse>

    @GET("api/profile")
    fun getProfile(@Header("Authorization")authorization: String): retrofit2.Call<ProfilResponse>
}