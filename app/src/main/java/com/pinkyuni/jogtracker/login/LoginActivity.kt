package com.pinkyuni.jogtracker.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pinkyuni.jogtracker.App
import com.pinkyuni.jogtracker.R
import com.pinkyuni.jogtracker.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: App.LoginViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewModel by lazy {
            ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        }
        viewModel.isLoggedIn.observe(this, Observer {
            when (it) {
                false -> Toast.makeText(this, getString(R.string.error_incorrect_uuid), Toast.LENGTH_SHORT).show()
                true -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })

        btnLogin.setOnClickListener {
            viewModel.uuidLogin(etUuid.text.toString())
        }
    }

}