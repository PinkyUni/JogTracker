package com.pinkyuni.jogtracker.data

import com.pinkyuni.jogtracker.data.entities.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET("auth/user")
    fun getCurrentUser(@Query("access_token") accessToken: String): Deferred<Response<User>>

    @GET("data/sync")
    fun getJogList(@Query("access_token") accessToken: String): Deferred<Response<RespJogs>>

    @POST("auth/uuidLogin")
    fun uuidLogin(@Body uuid: UuidLogin): Deferred<Response<LoggedIn>>

    @PUT("data/jog")
    fun updateJog(@Query("access_token") accessToken: String, @Body jog: JogUpdate): Deferred<Response<JogUpdate>>

    @POST("data/jog")
    fun addJog(@Query("access_token") accessToken: String, @Body jog: JogUpdate): Deferred<Response<JogUpdate>>

}
