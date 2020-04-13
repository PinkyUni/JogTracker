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
import java.lang.NumberFormatException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class JogFragment : Fragment() {

    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    private val viewModel by lazy {
        ViewModelProviders.of(activity as MainActivity).get(
            MainViewModel::class.java
        )
    }

    companion object {

        private const val argumentsJogKey = "jog"

        fun newInstance(jog: Jog?): JogFragment {
            val bundle = Bundle()
            bundle.putSerializable(argumentsJogKey, jog)
            val fragment = JogFragment()
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jog, flContainer, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jog = arguments?.getSerializable(argumentsJogKey) as Jog?
        if (jog != null) {
            val d = sdf.format(Date(jog.date))
            etDate.setText(d)

            etTime.setText(jog.time.toString())
            etDistance.setText(jog.distance.toString())

            btnSubmit.setOnClickListener {
                try {
                    jog.date = sdf.parse(etDate.text.toString())?.time ?: Date().time
                    jog.time = etTime.text.toString().toInt()
                    jog.distance = etDistance.text.toString().toFloat()
                    viewModel.updateJog(JogUpdate(jog))
                    (activity as MainActivity).supportFragmentManager.popBackStack()
                } catch (e: ParseException) {
                    Toast.makeText(
                        activity,
                        "Enter date as '${sdf.toPattern()}'",
                        Toast.LENGTH_SHORT
                    ).show()
                    etDate.requestFocus()
                } catch (e: NumberFormatException) {
                    Toast.makeText(
                        activity,
                        "Incorrect time or distance value",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            btnSubmit.setOnClickListener {
                try {
                    val parsed = sdf.parse(etDate.text.toString())
                    val j = Jog(
                        1,
                        1,
                        parsed!!.time,
                        etTime.text.toString().toInt(),
                        etDistance.text.toString().toFloat()
                    )
                    viewModel.addJog(JogUpdate(j))
                    (activity as MainActivity).supportFragmentManager.popBackStack()
                } catch (e: ParseException) {
                    Toast.makeText(
                        activity,
                        "Enter date as '${sdf.toPattern()}'",
                        Toast.LENGTH_SHORT
                    ).show()
                    etDate.requestFocus()
                } catch (e: NumberFormatException) {
                    Toast.makeText(
                        activity,
                        "Incorrect time or distance value",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}