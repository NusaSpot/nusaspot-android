package com.jpmedia.nusaspot.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ProfilResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: ProfilData? = null

    class ProfilData {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: String? = null

        @SerializedName("gender")
        @Expose
        var gender: String? = null

        @SerializedName("date_of_birth")
        @Expose
        var dateOfBirth: String? = null

        @SerializedName("profile_picture")
        @Expose
        var profilePicture: String? = null

        @SerializedName("phone")
        @Expose
        var phone: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }
}
