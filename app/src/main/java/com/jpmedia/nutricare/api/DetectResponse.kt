package com.jpmedia.nutricare.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetectResponse(
    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("data")
    @Expose
    val data: List<DetectDataItem>? = null
)

data class DetectDataItem(
    @SerializedName("id")
    @Expose
    val id: Int? = null,
    @SerializedName("user_id")
    @Expose
    val userId: String? = null,
    @SerializedName("status")
    @Expose
    val status: String? = null,
    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,
    @SerializedName("readable_created_at")
    @Expose
    val readable_created_at: String? = null,
    @SerializedName("created_time_at")
    @Expose
    val created_time_at: String? = null
)
