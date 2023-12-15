package com.jpmedia.nusaspot.api

data class FinishResponse(
    val status: String,
    val message: String,
    val data: Any?
)