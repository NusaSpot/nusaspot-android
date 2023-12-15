package com.jpmedia.nusaspot.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetectStartResponse(
    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("data")
    @Expose
    val data: ResponseData
)

data class ResponseData(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("user_id")
    @Expose
    val userId: String,

    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String
)