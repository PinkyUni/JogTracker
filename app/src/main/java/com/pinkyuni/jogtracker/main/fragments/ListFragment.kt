package com.pinkyuni.jogtracker.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinkyuni.jogtracker.R
import com.pinkyuni.jogtracker.main.JogAdapter
import com.pinkyuni.jogtracker.main.MainActivity
import com.pinkyuni.jogtracker.main.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(activity as MainActivity).get(
            MainViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, flContainer, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = JogAdapter(
            activity as MainActivity,
            listOf()
        ) {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.flContainer,
                    JogFragment(it)
                ).addToBackStack(null).commit()
        }
        rvJogs.layoutManager = LinearLayoutManager(activity)
        rvJogs.adapter = adapter

        fabAddJog.isEnabled = false
        viewModel.jogList.observe(activity as MainActivity, Observer {
            pbLoading?.visibility = View.VISIBLE
            adapter.setData(it)
            fabAddJog?.isEnabled = true
            pbLoading?.visibility = View.INVISIBLE
        })


        fabAddJog.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.flContainer,
                    JogFragment(null)
                ).addToBackStack(null).commit()
        }

        viewModel.added.observeForever {
            if (true == it) {
                Toast.makeText(activity, getString(R.string.msg_jog_successfully_added), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, getString(R.string.error_occurred), Toast.LENGTH_SHORT).show()
            }
        }

    }

}