package com.pinkyuni.jogtracker.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoggedIn(
    @SerializedName("access_token")
    var accessToken: String,
    @SerializedName("token_type")
    var tokenType: String,
    @SerializedName("expires_in")
    var expiresIn: Int,
    var scope: String,
    @SerializedName("created_at")
    var createdAt: Int
) : Serializable