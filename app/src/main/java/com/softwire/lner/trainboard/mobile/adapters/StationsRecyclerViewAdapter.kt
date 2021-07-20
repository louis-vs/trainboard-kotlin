package com.softwire.lner.trainboard.mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softwire.lner.trainboard.mobile.R
import com.softwire.lner.trainboard.mobile.models.Station

class StationsRecyclerViewAdapter(private val stations: List<Station>) : RecyclerView.Adapter<StationsRecyclerViewAdapter.StationsRecyclerViewHolder>() {

    class StationsRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stationItemTitle: TextView = view.findViewById(R.id.stationItemTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationsRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.station_item, parent, false)
        return StationsRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationsRecyclerViewHolder, position: Int) {
        holder.stationItemTitle.text = stations[position].displayName
    }

    override fun getItemCount(): Int {
        return stations.count()
    }

}
