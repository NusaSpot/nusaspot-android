package com.jpmedia.nusaspot.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("data")
    @Expose
    var data: UserData? = null
    class UserData {
        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("email")
        @Expose
        var email: String? = null
        @SerializedName("email_verified_at")
        @Expose
        var emailVerifiedAt: String? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
        @SerializedName("otp")
        @Expose
        var otp: String? = null
        @SerializedName("token")
        @Expose
        var token: String? = null
    }
}
