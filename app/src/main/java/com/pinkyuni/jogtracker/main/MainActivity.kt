package com.pinkyuni.jogtracker.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pinkyuni.jogtracker.App
import com.pinkyuni.jogtracker.ConnectionStateMonitor
import com.pinkyuni.jogtracker.R
import com.pinkyuni.jogtracker.main.fragments.ListFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: App.MainViewModelFactory by instance()
    private val hasConnection: ConnectionStateMonitor by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel by lazy {
            ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        }
        hasConnection.observe(this, Observer {
            when (it) {
                true -> {
                    ivNoConnection.visibility = View.GONE
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flContainer,
                        ListFragment()
                    ).commit()

                    viewModel.getUserInfo()
                }
                false -> {
                    Toast.makeText(this, "Check your Internet connection", Toast.LENGTH_SHORT)
                        .show()
                    ivNoConnection.visibility = View.VISIBLE
                }
            }
        })

    }

}
