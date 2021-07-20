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
import com.softwire.lner.trainboard.mobile.presenters.ApplicationPresenter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get references to views
        fromButton = findViewById(R.id.from_button)
        toButton = findViewById(R.id.to_button)
        searchButton = findViewById(R.id.search_btn)

        // add event listeners
        fromButton.setOnClickListener {
            startActivityForResult(Intent(this, ), )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    // CONTRACTUAL OBLIGATIONS

    override fun setTitle(title: String) {
        findViewById<TextView>(R.id.main_text).text = title
    }

    override fun setStations(stations: List<Station>) {
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, stations)
        fromButton.adapter = adapter
        toButton.adapter = adapter
        toButton.setSelection(1)
    }

    override fun openUrl(url: String) {
        val page = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, page)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun saveStations(stations: List<Station>) {
        //TODO("Not yet implemented")
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

    override fun launchSearchActivity() {
        //TODO("Not yet implemented")
    }

    override fun disableSearchButton() {
        searchButton.isEnabled = false
        searchButton.text = "Loading..."
    }
}
