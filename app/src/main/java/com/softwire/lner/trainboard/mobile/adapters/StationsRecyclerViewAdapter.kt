package com.softwire.lner.trainboard.mobile.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softwire.lner.trainboard.mobile.R
import com.softwire.lner.trainboard.mobile.models.Station
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify

class StationsRecyclerViewAdapter(private val stations: List<Station>, var context: Context) : RecyclerView.Adapter<StationsRecyclerViewAdapter.StationsRecyclerViewHolder>() {

    class StationsRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stationItemTitle: TextView = view.findViewById(R.id.stationItemTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationsRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.station_item, parent, false)
        return StationsRecyclerViewHolder(view)
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    override fun onBindViewHolder(holder: StationsRecyclerViewHolder, position: Int) {
        holder.stationItemTitle.text = stations[position].stationName

        holder.itemView.setOnClickListener {
            val result = Intent().putExtra("STATION", Json.stringify(stations[position]))
            (context as Activity).setResult(Activity.RESULT_OK, result)
            (context as Activity).finish()
        }
    }

    override fun getItemCount(): Int {
        return stations.count()
    }

}
