package com.pinkyuni.jogtracker.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.pinkyuni.jogtracker.App
import com.pinkyuni.jogtracker.R
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FeedbackDialogFragment : DialogFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: App.MainViewModelFactory by instance()
    private val topics = arrayListOf(1, 2, 3, 5, 8)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val viewModel by lazy {
            ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
        }
        val view = inflater.inflate(R.layout.feedback_dialog, null)
        builder.setView(view)
            .setPositiveButton(R.string.send_feedback) { dialog, id ->
                val text = view.findViewById(R.id.etText) as EditText
                val topic = view.findViewById(R.id.etTopicId) as EditText
                if (text.text.isNotEmpty() && topic.text.isNotEmpty()) {
                    val tid = topic.text.toString().toInt()
                    if (tid in topics) {
                        viewModel.sendFeedback(tid, text.text.toString())
                    } else {
                        Toast.makeText(activity, getString(R.string.error_invalid_topic_id), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, getString(R.string.error_fill_all_fields), Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                this@FeedbackDialogFragment.dialog?.cancel()
            }
        return builder.create()
    }

}