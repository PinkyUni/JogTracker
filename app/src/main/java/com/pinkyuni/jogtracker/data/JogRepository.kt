package com.pinkyuni.jogtracker.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.pinkyuni.jogtracker.data.entities.*

class JogRepository(application: Application, private val apiService: APIService) {

    private val sharedPreferencesKey = "com.pinkyuni.jogtracker.prefs"
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
    private var accessToken: String? = null
    val isLoggedIn = MutableLiveData<Boolean>()

    init {
        accessToken = sharedPreferences.getString("access_token", null)
        isLoggedIn.postValue(accessToken != null)
    }

    suspend fun uuidLogin(uuid: String): LoggedIn? {
        val loggedIn = apiService.uuidLogin(UuidLogin(uuid)).await().body()
        accessToken = loggedIn?.accessToken
        isLoggedIn.postValue(accessToken != null)
        sharedPreferences.edit().putString("access_token", accessToken).apply()
        return loggedIn
    }

    suspend fun getCurrentUser(): User? {
        accessToken?.let {
            return apiService.getCurrentUser(it).await().body()
        }
        return null
    }

    suspend fun getJogList(): List<Jog>? {
        accessToken?.let {
            return apiService.getJogList(it).await().body()?.jogs
        }
        return null
    }

    suspend fun updateJog(jog: JogUpdate): JogUpdate? {
        accessToken?.let {
            return apiService.updateJog(it, jog).await().body()
        }
        return null
    }

    suspend fun addJog(jog: JogUpdate): JogUpdate? {
        accessToken?.let {
            return apiService.addJog(it, jog).await().body()
        }
        return null
    }

    suspend fun sendFeedback(feedback: Feedback): String? {
        accessToken?.let {
            return apiService.sendFeedback(it, feedback).await().body()
        }
        return null
    }

}