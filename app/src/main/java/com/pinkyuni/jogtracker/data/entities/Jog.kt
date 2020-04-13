package com.pinkyuni.jogtracker.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Jog(
    val id: Long,
    @SerializedName("user_id")
    val userId: Long,
    var date: Long,
    var time: Int,
    var distance: Float
) : Serializable {



}