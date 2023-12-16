package com.jpmedia.nusaspot.api
data class FinishResponse(
    val status: String,
    val message: String,
    val data: List<Recipe>?
)
data class Recipe(
    val id: Int,
    val image: String,
    val title: String,
    val ingredients: String,
    val tutorials: String,
    val created_at: String,
    val updated_at: String,
    val category: String
)

