package com.pinkyuni.jogtracker.main

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
    private val clickListener: (Jog) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private var list = mutableListOf<Jog>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.jog_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].let { holder.bind(it, clickListener) }
    }

    internal fun setItems(items: List<Jog>?) {
        items?.let {
            list.clear()
            list.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun getItemId(position: Int): Long {
        return list[position].id
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