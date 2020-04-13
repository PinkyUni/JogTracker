package com.pinkyuni.jogtracker.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.pinkyuni.jogtracker.data.JogRepository
import com.pinkyuni.jogtracker.data.entities.Feedback
import com.pinkyuni.jogtracker.data.entities.Jog
import com.pinkyuni.jogtracker.data.entities.JogUpdate
import kotlinx.coroutines.launch

class MainViewModel(application: Application, private val repository: JogRepository) :
    AndroidViewModel(application) {

    var jogList = MutableLiveData<List<Jog>>()
    private val _added = LiveEvent<Boolean>()
    val added: LiveData<Boolean> = _added
    private val _feedbackStatus = LiveEvent<Boolean>()
    val feedbackStatus: LiveData<Boolean> = _feedbackStatus
    private val _isLoadingVisible = MutableLiveData<Boolean>()
    val isLoadingVisible: LiveData<Boolean> = _isLoadingVisible

    private fun getUser() {
        viewModelScope.launch {
            repository.getCurrentUser()
        }
    }

    fun getUserInfo() {
        getUser()
        getJogList()
    }

    private fun getJogList() {
        _isLoadingVisible.value = true
        viewModelScope.launch {
            val list = repository.getJogList()
            jogList.postValue(list)
            _isLoadingVisible.postValue(false)
        }
    }

    fun updateJog(jog: JogUpdate) {
        viewModelScope.launch {
            repository.updateJog(jog)
        }
    }

    fun addJog(jog: JogUpdate) {
        viewModelScope.launch {
            val addedJog = repository.addJog(jog)
            addedJog?.let {
                if (addedJog.updatedAt != null) {
                    _added.postValue(true)
                }
            }
        }
    }

    fun sendFeedback(topic: Int, text: String) {
        viewModelScope.launch {
            val res: String? = repository.sendFeedback(Feedback(topic, text))
            val b = res?.equals("ok", true)
            _feedbackStatus.postValue(b)
        }
    }

}