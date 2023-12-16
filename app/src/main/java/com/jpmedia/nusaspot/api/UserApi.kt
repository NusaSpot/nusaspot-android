package com.jpmedia.nusaspot.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UserApi {
    @POST("api/login")
    fun  login(
        @Body userRequest: UserRequest
    ): retrofit2.Call<UserResponse>

    @GET("api/login-status")
    fun getUsers(@Header("Authorization") authorization: String,
                 @Header("Content-Type") contentType: String = "application/json"): retrofit2.Call<UserResponse>

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

    @GET("api/login/google")
    fun googleLogin()

    @GET("api/detect")
    fun getDetect(@Header("Authorization")
                  authorization: String,
                  @Header("Accept") accept: String = "application/json")
            : retrofit2.Call<DetectResponse>

    @GET("api/detect-detail/{detectId}")
    fun getDetailDetect(
        @Path("detectId") detectId: String,
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String = "application/json")
            : retrofit2.Call<DetectDetailResponse>

    @GET("api/detect-start")
    fun getDetectStart(
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String = "application/json"
    ): retrofit2.Call<DetectStartResponse>

    @Multipart
    @POST("api/detect-detail-store/{detectId}")
    fun postDetect(
        @Header("Authorization") authorization: String,
        @Part image : MultipartBody.Part,
        @Path("detectId") detectId: String,
        @Header("Accept") accept: String = "application/json"
    ): retrofit2.Call<PostDetectResponse>

    @Multipart
    @POST("api/profile")
    fun postProfil(
        @Header("Authorization") authorization: String,
        @Part profile_picture: MultipartBody.Part,
        @Part("phone") phone: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("date_of_birth") date_of_birth: RequestBody,
    ):retrofit2.Call<ProfilResponse>

    @GET("api/detect-finish/{detectId}")
    fun detectFinish(
        @Header("Authorization") authorization: String,
        @Path("detectId") detectId: String,
        @Header("Accept") accept: String = "application/json"
    ):retrofit2.Call<FinishResponse>

    @GET("/api/detect-detail-delete/{detectId}/{id}")
    fun deleteDetect(
        @Header("Authorization") authorization: String,
        @Path("detectId") detectId: String,
        @Path("id") id: Int,
        @Header("Accept") accept: String = "application/json"
    ):retrofit2.Call<DeleteResponse>

    @POST("api/reset-password")
    fun resetPassword(
        @Body userRequest: UserRequest
    ):retrofit2.Call<ChangePasswordResponse>

    @GET("/api/recipe/{id}")
    fun getDetailRecipe(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Header("Accept") accept: String = "application/json"
    ):retrofit2.Call<DetailResepResponse>
}