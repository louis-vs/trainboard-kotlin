package com.softwire.lner.trainboard.mobile.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softwire.lner.trainboard.mobile.R
import com.softwire.lner.trainboard.mobile.adapters.ResultsRecyclerViewAdapter
import com.softwire.lner.trainboard.mobile.contracts.ApplicationContract
import com.softwire.lner.trainboard.mobile.models.JourneyCollection
import com.softwire.lner.trainboard.mobile.models.Station
import com.softwire.lner.trainboard.mobile.models.StationCollection
import com.softwire.lner.trainboard.mobile.presenters.ApplicationPresenter
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.*
import kotlinx.serialization.stringify

/**
 * The main view for the Android app
 */
class MainActivity : AppCompatActivity(), ApplicationContract.View {

    private lateinit var presenter: ApplicationContract.Presenter

    private lateinit var fromButton: Button
    private lateinit var toButton: Button
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var stations: List<Station>

    private var fromStation: Station? = null
    private var toStation: Station? = null

    companion object {
        internal const val PICK_FROM_STATION_REQUEST = 0
        internal const val PICK_TO_STATION_REQUEST = 1
    }

    @ImplicitReflectionSerializer
    @UnstableDefault
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get references to views
        fromButton = findViewById(R.id.from_button)
        toButton = findViewById(R.id.to_button)
        searchButton = findViewById(R.id.search_btn)

        // add event listeners
        fromButton.setOnClickListener {
            launchSearchActivity(PICK_FROM_STATION_REQUEST)
        }
        toButton.setOnClickListener {
            launchSearchActivity(PICK_TO_STATION_REQUEST)
        }
        searchButton.setOnClickListener {
            if (fromStation != null && toStation != null) {
                presenter.runSearch(
                        fromStation!!,
                        toStation!!
                )
            }
        }

        // get reference to presenter and tell it we're done
        presenter = ApplicationPresenter()
        presenter.onViewTaken(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        when (requestCode) {
            PICK_FROM_STATION_REQUEST ->
                if (resultCode == RESULT_OK) {
                    fromStation = stations.find { it.crs == intent?.dataString }
                }
            PICK_TO_STATION_REQUEST ->
                if (resultCode == RESULT_OK) {
                    toStation = stations.find { it.crs == intent?.dataString }
                }
        }
    }

    // CONTRACTUAL OBLIGATIONS

    override fun setTitle(title: String) {
        findViewById<TextView>(R.id.main_text).text = title
    }

    override fun setStations(stations: List<Station>) {
        this.stations = stations
    }

    override fun openUrl(url: String) {
        val page = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, page)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun saveStations(stations: List<Station>) {
        this.stations = stations
    }

    override fun displayErrorMessage(message: String) {
       Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun displayJourneys(journeyCollection: JourneyCollection) {
        val adapter = ResultsRecyclerViewAdapter(journeyCollection)

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

    @ImplicitReflectionSerializer
    @UnstableDefault
    private fun launchSearchActivity(requestCode: Int) {
        val intent = Intent(this, StationSearchActivity::class.java)
        intent.putExtra("LIST", Json.stringify(StationCollection(stations)))
        startActivityForResult(intent, requestCode)
    }

    override fun disableSearchButton() {
        searchButton.isEnabled = false
        searchButton.text = "Loading..."
    }
}
