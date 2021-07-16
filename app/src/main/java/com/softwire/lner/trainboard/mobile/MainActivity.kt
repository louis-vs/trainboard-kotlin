package com.softwire.lner.trainboard.mobile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetbrains.handson.mpp.mobile.*

/**
 * The main view for the Android app
 */
class MainActivity : AppCompatActivity(), ApplicationContract.View {

    private lateinit var presenter: ApplicationContract.Presenter

    private lateinit var fromSpinner: Spinner
    private lateinit var toSpinner: Spinner
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get references to views
        fromSpinner = findViewById(R.id.from_spinner)
        toSpinner = findViewById(R.id.to_spinner)
        searchButton = findViewById(R.id.search_btn)

        // add event listeners
        searchButton.setOnClickListener {
            presenter.runSearch(
                    fromSpinner.selectedItem as Station,
                    toSpinner.selectedItem as Station
            )
        }

        // get reference to presenter and tell it we're done
        presenter = ApplicationPresenter()
        presenter.onViewTaken(this)
    }

    override fun setTitle(title: String) {
        findViewById<TextView>(R.id.main_text).text = title
    }

    override fun setStations(stations: List<Station>) {
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, stations)
        fromSpinner.adapter = adapter
        toSpinner.adapter = adapter
        toSpinner.setSelection(1)
    }

    override fun openUrl(url: String) {
        val page = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, page)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun displayErrorMessage(message: String) {
       Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun displayJourneys(journeyCollection: JourneyCollection) {
        val adapter = RecyclerViewAdapter(journeyCollection)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // add divider line
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun enableSearchButton() {
        searchButton.isEnabled = true
        searchButton.text = "Search"
    }

    override fun disableSearchButton() {
        searchButton.isEnabled = false
        searchButton.text = "Loading..."
    }
}


class RecyclerViewAdapter(private val dataSet: JourneyCollection) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val departureTimeView: TextView = view.findViewById(R.id.departureTime)
        val arrivalTimeView: TextView = view.findViewById(R.id.arrivalTime)
        val departureStationView: TextView = view.findViewById(R.id.departureStation)
        val arrivalStationView: TextView = view.findViewById(R.id.arrivalStation)
        val statusView: TextView = view.findViewById(R.id.statusText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.result_item, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
        val journey = dataSet.outboundJourneys[position]
        viewHolder.departureTimeView.text = journey.departureTimeFormatted
        viewHolder.arrivalTimeView.text = journey.arrivalTimeFormatted
        viewHolder.departureStationView.text = journey.originStation.displayName
        viewHolder.arrivalStationView.text = journey.destinationStation.displayName
        viewHolder.statusView.text = journey.status
    }

    override fun getItemCount() = dataSet.outboundJourneys.size
}
