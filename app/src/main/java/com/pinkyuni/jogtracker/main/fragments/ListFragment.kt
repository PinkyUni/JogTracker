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
import kotlinx.android.synthetic.main.fragment_list.view.*

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
        val view = inflater.inflate(R.layout.fragment_list, flContainer, false)
        view.fabAddJog.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.flContainer,
                    JogFragment.newInstance(null)
                ).addToBackStack(null).commit()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = JogAdapter {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.flContainer,
                    JogFragment.newInstance(it)
                ).addToBackStack(null).commit()
        }
        adapter.setHasStableIds(true)
        rvJogs.layoutManager = LinearLayoutManager(activity)
        rvJogs.adapter = adapter

        viewModel.isLoadingVisible.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> pbLoading?.visibility = View.VISIBLE
                false -> {
                    pbLoading?.visibility = View.INVISIBLE
                    adapter.setItems(viewModel.jogList.value)
                }
            }
        })

        viewModel.added.observe(viewLifecycleOwner, Observer {
            if (true == it) {
                Toast.makeText(
                    activity,
                    getString(R.string.msg_jog_successfully_added),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(activity, getString(R.string.error_occurred), Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }

}