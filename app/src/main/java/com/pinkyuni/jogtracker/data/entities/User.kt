package com.pinkyuni.jogtracker.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    val id: String,
    var email: String,
    var phone: String,
    var role: String,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String
): Serializable