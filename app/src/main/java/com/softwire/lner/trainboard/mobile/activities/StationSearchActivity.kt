package com.softwire.lner.trainboard.mobile.activities

import android.os.Bundle
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
import com.softwire.lner.trainboard.mobile.presenters.SearchPresenter

class StationSearchActivity : AppCompatActivity(), SearchContract.View {
    private lateinit var presenter: SearchContract.Presenter

    private lateinit var searchTitle: TextView
    private lateinit var searchInput: EditText
    private lateinit var stationsRecyclerView: RecyclerView

    private lateinit var stations: List<Station>
    private lateinit var stationsAdapter: StationsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stations)

        searchTitle = findViewById(R.id.searchTitle)
        searchInput = findViewById(R.id.searchInput)
        stationsRecyclerView = findViewById(R.id.stationsRecyclerView)

        stations = listOf()
        stationsAdapter = StationsRecyclerViewAdapter(stations)
        stationsRecyclerView.adapter = stationsAdapter

        val layoutManager = LinearLayoutManager(this)
        stationsRecyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(stationsRecyclerView.context, layoutManager.orientation)
        stationsRecyclerView.addItemDecoration(dividerItemDecoration)

        presenter = SearchPresenter()
        presenter.onViewTaken(this)
    }

    // CONTRACTUAL OBLIGATIONS

    override fun setTitle(title: String) {
        searchTitle.text = title
    }

    override fun setStations(stations: List<Station>) {
        this.stations = stations

        stationsAdapter.notifyDataSetChanged()
    }

    override fun displayErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
