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

        @SerializedName("name")
        @Expose
        var name: String? = null

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

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("weight")
        @Expose
        var weight: String? = null

        @SerializedName("height")
        @Expose
        var height: String? = null

        @SerializedName("body_status")
        @Expose
        var body_status: String? = null

        @SerializedName("quotes")
        @Expose
        var quotes: String? = null

        @SerializedName("is_guest")
        @Expose
        var is_guest: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }
}
