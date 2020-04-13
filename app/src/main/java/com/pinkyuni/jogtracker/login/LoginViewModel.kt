package com.pinkyuni.jogtracker.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pinkyuni.jogtracker.data.JogRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application, private val repository: JogRepository) :
    AndroidViewModel(application)
{

    val isLoggedIn = repository.isLoggedIn

    fun uuidLogin(uuid: String) {
        viewModelScope.launch {
            repository.uuidLogin(uuid)
        }
    }

}