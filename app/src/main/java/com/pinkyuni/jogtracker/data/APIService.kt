package com.pinkyuni.jogtracker.data

import com.pinkyuni.jogtracker.data.entities.*
import retrofit2.http.*

interface APIService {

    @FormUrlEncoded
    @Headers("Authentication: true")
    @POST("auth/uuidLogin")
    suspend fun uuidLogin(@Field("uuid") uuid: String): LoggedIn?

    @GET("auth/user")
    suspend fun getCurrentUser(): User

    @GET("data/sync")
    suspend fun getJogList(): RespJogs

    @PUT("data/jog")
    suspend fun updateJog(@Body jog: JogUpdate): JogUpdate

    @POST("data/jog")
    suspend fun addJog(@Body jog: JogUpdate): JogUpdate

    @POST("feedback/send")
    suspend fun sendFeedback(@Body feedback: Feedback): String

}
