package com.pinkyuni.jogtracker

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinkyuni.jogtracker.data.JogRepository
import com.pinkyuni.jogtracker.login.LoginViewModel
import com.pinkyuni.jogtracker.main.MainViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.*

class App : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(networkModule)
        constant(tag = "date_format") with "dd.MM.yyyy"
        bind<Application>() with singleton { this@App }
        bind<JogRepository>() with singleton { JogRepository(instance(), instance()) }
        bind<MainViewModelFactory>() with provider { MainViewModelFactory(instance(), instance()) }
        bind<LoginViewModelFactory>() with provider {
            LoginViewModelFactory(
                instance(),
                instance()
            )
        }
    }

    companion object {
        operator fun get(context: Context): App {
            return context.applicationContext as App
        }
    }

    @Suppress("UNCHECKED_CAST")
    class MainViewModelFactory(
        private val application: Application,
        private val repository: JogRepository
    ) : ViewModelProvider.AndroidViewModelFactory(application) {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(application, repository) as T
        }

    }

    @Suppress("UNCHECKED_CAST")
    class LoginViewModelFactory(
        private val application: Application,
        private val repository: JogRepository
    ) : ViewModelProvider.AndroidViewModelFactory(application) {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(application, repository) as T
        }

    }

}