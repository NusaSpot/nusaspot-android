package com.jpmedia.nutricare.api

import com.google.gson.annotations.SerializedName

data class DetailResepResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: RecipeData
)

data class RecipeData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("image")
    val image: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("ingredients")
    val ingredients: String,

    @SerializedName("tutorials")
    val tutorials: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("category")
    val category: String
)
