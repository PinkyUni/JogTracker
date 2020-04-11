package com.pinkyuni.jogtracker.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pinkyuni.jogtracker.data.JogRepository
import com.pinkyuni.jogtracker.data.entities.Jog
import com.pinkyuni.jogtracker.data.entities.JogUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(application: Application, private val repository: JogRepository) :
    AndroidViewModel(application) {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    var jogList = MutableLiveData<List<Jog>>()
    var added = MutableLiveData<Boolean>()

    private fun getUser() {
        scope.launch {
            repository.getCurrentUser()
        }
    }

    fun getUserInfo() {
        getUser()
        getJogList()
    }

    private fun getJogList() {
        scope.launch {
            val list = repository.getJogList()
            jogList.postValue(list)
        }
    }

    fun updateJog(jog: JogUpdate) {
        scope.launch {
            repository.updateJog(jog)
        }
    }

    fun addJog(jog: JogUpdate) {
        scope.launch {
            val addedJog = repository.addJog(jog)
            addedJog?.let {
                if (addedJog.updatedAt != null) {
                    added.postValue(true)
                }
            }
        }
    }

}