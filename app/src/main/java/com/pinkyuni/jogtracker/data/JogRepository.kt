package com.pinkyuni.jogtracker.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.hadilq.liveevent.LiveEvent
import com.pinkyuni.jogtracker.data.entities.*
import retrofit2.HttpException

class JogRepository(application: Application, private val apiService: APIService) {

    private var accessToken: String? = null
    private val _isLoggedIn = LiveEvent<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    init {
        val sharedPreferencesKey = "com.pinkyuni.jogtracker.prefs"
        val sharedPreferences: SharedPreferences =
            application.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        accessToken = sharedPreferences.getString("access_token", null)
        if (accessToken != null)
            _isLoggedIn.postValue(true)
    }

    suspend fun uuidLogin(uuid: String): LoggedIn? {
        var loggedIn: LoggedIn? = null
        try {
            loggedIn = apiService.uuidLogin(uuid)
        } catch (e: HttpException) {
            _isLoggedIn.postValue(false)
        }
        _isLoggedIn.postValue(loggedIn != null)
        return loggedIn
    }

    suspend fun getCurrentUser(): User? {
        return apiService.getCurrentUser()
    }

    suspend fun getJogList(): List<Jog>? {
        return apiService.getJogList().jogs
    }

    suspend fun updateJog(jog: JogUpdate): JogUpdate? {
        return apiService.updateJog(jog)
    }

    suspend fun addJog(jog: JogUpdate): JogUpdate? {
        return apiService.addJog(jog)
    }

    suspend fun sendFeedback(feedback: Feedback): String? {
        return apiService.sendFeedback(feedback)
    }

}