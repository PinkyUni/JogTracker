package com.pinkyuni.jogtracker.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pinkyuni.jogtracker.R
import com.pinkyuni.jogtracker.data.entities.Jog
import kotlinx.android.synthetic.main.jog_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class JogAdapter(
    private val context: Context,
    private var list: List<Jog>?,
    private val clickListener: (Jog) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.jog_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.get(position)?.let { holder.bind(it, clickListener) }
    }

    internal fun setData(items: List<Jog>?) {
        this.list = items
        notifyDataSetChanged()
    }
}

class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(jog: Jog, clickListener: (Jog) -> Unit) {
        val sdf = SimpleDateFormat("d MMM_yyyy", Locale.US)
        val d = sdf.format(Date(jog.date))
        val date = d.split("_")
        view.tvDay.text = date[0]
        view.tvYear.text = date[1]
        view.tvTime.text = "${jog.time}s"
        view.tvDistance.text = "${jog.distance}m"
        view.setOnClickListener { clickListener(jog) }
    }

}