package com.jpmedia.nusaspot.api

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose


data class DetectDetailResponse (
    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("message")
    @Expose
    var message: String? = null,

    @SerializedName("data")
    @Expose
    val data: kotlin.collections.List<DetailData>? = null
)
    data class DetailData (
        @SerializedName("id")
        @Expose
        var id: Int? = null,

        @SerializedName("detect_id")
        @Expose
        var detectId: String? = null,

        @SerializedName("image")
        @Expose
        var image: String? = null,

        @SerializedName("result")
        @Expose
        var result: Any? = null,

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null,

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    )
