package com.jpmedia.nusaspot.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserRequest {

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null
    @SerializedName("password_confirmation")
    @Expose
    var password_confirmation: String? = null

    @SerializedName("otp")
    @Expose
    var otp: String? = null
}
