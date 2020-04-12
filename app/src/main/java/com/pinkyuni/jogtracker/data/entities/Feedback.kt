package com.pinkyuni.jogtracker.data.entities

import com.google.gson.annotations.SerializedName

data class Feedback(@SerializedName("topic_id") val topic: Int, val text: String)