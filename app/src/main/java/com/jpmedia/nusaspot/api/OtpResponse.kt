package com.jpmedia.nusaspot.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OtpResponse (
    @SerializedName("status")
    @Expose
    var status: String?,
    @SerializedName("message")
    @Expose
    var message: String?,
    @SerializedName("data")
    @Expose
    var data: Any?
)