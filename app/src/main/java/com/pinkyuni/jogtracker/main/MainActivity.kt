package com.pinkyuni.jogtracker.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
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

    private val tag = "feedbackDialog"
    override val kodein by closestKodein()
    private val viewModelFactory: App.MainViewModelFactory by instance()
    private val hasConnection: ConnectionStateMonitor by instance()
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toggle = ActionBarDrawerToggle(
            this,
            dlMenu,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        dlMenu.addDrawerListener(
            toggle
        )
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        nvMenu.setNavigationItemSelectedListener {
            if (it.itemId == R.id.miSendFeedback) {
                val dialogFragment =
                    supportFragmentManager.findFragmentByTag(tag) as DialogFragment?
                if (dialogFragment == null) {
                    val fragment by instance<DialogFragment>()
                    fragment.tag
                    fragment.show(supportFragmentManager, tag)
                }
            }
            return@setNavigationItemSelectedListener true
        }
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
                    Toast.makeText(
                        this,
                        getString(R.string.error_check_internet_connection),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    ivNoConnection.visibility = View.VISIBLE
                }
            }
        })
        viewModel.feedbackStatus.observe(this, Observer {
            when (it) {
                true -> Toast.makeText(
                    this,
                    getString(R.string.msg_feedback_sent),
                    Toast.LENGTH_SHORT
                ).show()
                false -> Toast.makeText(
                    this,
                    getString(R.string.error_occurred),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
