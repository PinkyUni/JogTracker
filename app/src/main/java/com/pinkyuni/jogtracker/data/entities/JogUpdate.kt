package com.pinkyuni.jogtracker.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigInteger
import java.util.*

data class JogUpdate(
    @SerializedName("jog_id")
    var jogId: Long,
    @SerializedName("user_id")
    var userId: Long,
    var date: Date,
    var time: Int,
    var distance: Float,
    @SerializedName("created_at")
    val createdAt: Date?,
    @SerializedName("updated_at")
    val updatedAt: Date?
) : Serializable {

    constructor(jog: Jog) : this(
        jog.id,
        jog.userId,
        Date(jog.date),
        jog.time,
        jog.distance,
        null,
        null
    )

}