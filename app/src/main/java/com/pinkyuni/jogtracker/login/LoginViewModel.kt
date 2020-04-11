package com.pinkyuni.jogtracker.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.pinkyuni.jogtracker.data.JogRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginViewModel(application: Application, private val repository: JogRepository) :
    AndroidViewModel(application)
{

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val accessToken: LiveData<Boolean?> = repository.isLoggedIn

    fun uuidLogin(uuid: String) {
        scope.launch {
            repository.uuidLogin(uuid)
        }
    }


}