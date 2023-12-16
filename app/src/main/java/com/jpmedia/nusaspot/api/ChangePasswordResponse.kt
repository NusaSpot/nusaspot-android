package com.jpmedia.nusaspot.api

data class ChangePasswordResponse(
    val status: String,
    val message: String,
    val data: UserData
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val email_verified_at: String,
    val created_at: String,
    val updated_at: String,
    val otp: String,
    val is_guest: String,
    val token: String
)

