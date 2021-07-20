package com.softwire.lner.trainboard.mobile.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softwire.lner.trainboard.mobile.R
import com.softwire.lner.trainboard.mobile.models.JourneyCollection

class ResultsRecyclerViewAdapter(private val dataSet: JourneyCollection) : RecyclerView.Adapter<ResultsRecyclerViewAdapter.ResultsRecyclerViewHolder>() {

    class ResultsRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val departureTimeView: TextView = view.findViewById(R.id.departureTime)
        val arrivalTimeView: TextView = view.findViewById(R.id.arrivalTime)
        val arrivalExtraDayView: TextView = view.findViewById(R.id.arrivalExtraDay)
        val departureStationView: TextView = view.findViewById(R.id.departureStation)
        val arrivalStationView: TextView = view.findViewById(R.id.arrivalStation)
        val statusView: TextView = view.findViewById(R.id.statusText)
        val statusDrawable: GradientDrawable = statusView.background as GradientDrawable
        val arrowView: ImageView = view.findViewById(R.id.arrow)
        val arrowDrawable: GradientDrawable = arrowView.background as GradientDrawable
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.result_item, parent, false)
        return ResultsRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ResultsRecyclerViewHolder, position: Int) {
        val journey = dataSet.outboundJourneys[position]
        viewHolder.departureTimeView.text = journey.departureTimeFormatted
        viewHolder.arrivalTimeView.text = journey.arrivalTimeFormatted
        viewHolder.arrivalExtraDayView.text = journey.extraDay
        viewHolder.departureStationView.text = journey.originStation.displayName
        viewHolder.arrivalStationView.text = journey.destinationStation.displayName
        viewHolder.statusView.text = journey.status.statusText
        val backgroundColor = journey.status.backgroundColor
        viewHolder.statusDrawable.setColor(Color.argb(backgroundColor.alpha, backgroundColor.red, backgroundColor.green, backgroundColor.blue))
        viewHolder.arrowDrawable.setColor(Color.argb(backgroundColor.alpha, backgroundColor.red, backgroundColor.green, backgroundColor.blue))
        val textColor = journey.status.textColor
        viewHolder.statusView.setTextColor(Color.argb(textColor.alpha, textColor.red, textColor.green, textColor.blue))
    }

    override fun getItemCount() = dataSet.outboundJourneys.size
}
