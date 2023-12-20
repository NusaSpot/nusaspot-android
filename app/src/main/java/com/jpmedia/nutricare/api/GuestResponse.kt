package com.jpmedia.nutricare.api

import com.google.gson.annotations.SerializedName

data class GuestResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: GuestData
)

data class GuestData(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("is_guest")
    val isGuest: Boolean,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("token")
    val token: String
)
