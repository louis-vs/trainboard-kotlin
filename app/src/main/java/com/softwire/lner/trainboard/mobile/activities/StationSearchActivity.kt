package com.softwire.lner.trainboard.mobile.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.softwire.lner.trainboard.mobile.R
import com.softwire.lner.trainboard.mobile.contracts.SearchContract
import com.softwire.lner.trainboard.mobile.models.Station
import com.softwire.lner.trainboard.mobile.presenters.SearchPresenter

class StationSearchActivity : AppCompatActivity(), SearchContract.View {
    private lateinit var presenter: SearchContract.Presenter

    private lateinit var searchTitle: TextView
    private lateinit var searchInput: EditText
    private lateinit var stationsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stations)

        searchTitle = findViewById(R.id.searchTitle)
        searchInput = findViewById(R.id.searchInput)
        stationsRecyclerView = findViewById(R.id.stationsRecyclerView)

        presenter = SearchPresenter()
        presenter.onViewTaken(this)
    }

    // CONTRACTUAL OBLIGATIONS

    override fun setTitle(title: String) {
        searchTitle.text = title
    }

    override fun setStations(stations: List<Station>) {
        TODO("Not yet implemented")
    }

    override fun displayErrorMessage(message: String) {
        TODO("Not yet implemented")
    }
}
