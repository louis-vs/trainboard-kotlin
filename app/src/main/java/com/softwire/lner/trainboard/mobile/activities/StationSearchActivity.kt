package com.softwire.lner.trainboard.mobile.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softwire.lner.trainboard.mobile.R
import com.softwire.lner.trainboard.mobile.adapters.StationsRecyclerViewAdapter
import com.softwire.lner.trainboard.mobile.contracts.SearchContract
import com.softwire.lner.trainboard.mobile.models.Station
import com.softwire.lner.trainboard.mobile.models.StationCollection
import com.softwire.lner.trainboard.mobile.presenters.SearchPresenter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse

class StationSearchActivity : AppCompatActivity(), SearchContract.View {
    private lateinit var presenter: SearchContract.Presenter

    private lateinit var searchTitle: TextView
    private lateinit var searchInput: EditText
    private lateinit var stationsRecyclerView: RecyclerView

    private val filteredStations: MutableList<Station> = mutableListOf()
    private lateinit var allStations: List<Station>
    private lateinit var stationsAdapter: StationsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stations)

        searchTitle = findViewById(R.id.searchTitle)
        searchInput = findViewById(R.id.searchInput)
        stationsRecyclerView = findViewById(R.id.stationsRecyclerView)

        val collection: StationCollection = Json.parse(StationCollection.serializer(), intent.getStringExtra("LIST"))
        allStations = collection.stations
        filteredStations.clear()
        filteredStations.addAll(allStations)
        stationsAdapter = StationsRecyclerViewAdapter(filteredStations, this)
        stationsRecyclerView.adapter = stationsAdapter

        val layoutManager = LinearLayoutManager(this)
        stationsRecyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(stationsRecyclerView.context, layoutManager.orientation)
        stationsRecyclerView.addItemDecoration(dividerItemDecoration)


        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                presenter.filterStations(searchInput.text.toString(), allStations)
            }
        })

        presenter = SearchPresenter()
        presenter.onViewTaken(this)
    }

    // CONTRACTUAL OBLIGATIONS

    override fun setTitle(title: String) {
        searchTitle.text = title
    }

    override fun displayStations(stations: List<Station>) {
        filteredStations.clear()
        filteredStations.addAll(stations)

        stationsAdapter.notifyDataSetChanged()
    }

    override fun displayErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
