package com.pinkyuni.jogtracker.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigInteger

data class Jog(
    val id: BigInteger,
    @SerializedName("user_id")
    val userId: BigInteger,
    var date: Long,
    var time: Int,
    var distance: Float
) : Serializable {



}