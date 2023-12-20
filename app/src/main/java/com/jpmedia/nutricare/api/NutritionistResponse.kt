package com.jpmedia.nutricare.api



data class NutritionistResponse(
    val status: String,
    val message: String,
    val data: List<NutritionistData>
)

data class NutritionistData(
    val id: Int,
    val name: String,
    val email: String,
    val email_verified_at: String?,
    val password: String,
    val password_status: String,
    val remember_token: String?,
    val created_at: String,
    val updated_at: String,
    val is_eligible: String,
    val remark: String?,
    val deleted_at: String?,
    val nutritionist_profile: NutritionistProfile
)

data class NutritionistProfile(
    val id: Int,
    val nutritionist_id: String,
    val profile_picture: String,
    val gender: String,
    val date_of_birth: String,
    val phone: String,
    val nik: String,
    val work_experience: String,
    val education: String,
    val work_place: String,
    val cv: String,
    val created_at: String,
    val updated_at: String
)
