package com.pinkyuni.jogtracker.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pinkyuni.jogtracker.R
import com.pinkyuni.jogtracker.data.entities.Jog
import com.pinkyuni.jogtracker.data.entities.JogUpdate
import com.pinkyuni.jogtracker.main.MainActivity
import com.pinkyuni.jogtracker.main.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_jog.*
import java.math.BigInteger
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class JogFragment(private val jog: Jog?) : Fragment() {

    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)

    private val viewModel by lazy {
        ViewModelProviders.of(activity as MainActivity).get(
            MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jog, flContainer, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (jog != null) {
            val d = sdf.format(Date(jog.date))
            etDate.setText(d)

            etTime.setText(jog.time.toString())
            etDistance.setText(jog.distance.toString())

            btnSubmit.setOnClickListener {
                jog.date = sdf.parse(etDate.text.toString())?.time ?: Date().time
                jog.time = etTime.text.toString().toInt()
                jog.distance = etDistance.text.toString().toFloat()
                viewModel.updateJog(JogUpdate(jog))
                (activity as MainActivity).supportFragmentManager.popBackStack()
            }
        } else {
            btnSubmit.setOnClickListener {
                try {
                    val parsed = sdf.parse(etDate.text.toString())
                    val j = Jog(
                        BigInteger("1"),
                        BigInteger("1"),
                        parsed!!.time,
                        etTime.text.toString().toInt(),
                        etDistance.text.toString().toFloat()
                    )
                    viewModel.addJog(JogUpdate(j))
                    (activity as MainActivity).supportFragmentManager.popBackStack()
                } catch (e: ParseException) {
                    Toast.makeText(activity, "Enter date as '${sdf.toPattern()}'", Toast.LENGTH_SHORT).show()
                    etDate.requestFocus()
                }
            }
        }
    }

}