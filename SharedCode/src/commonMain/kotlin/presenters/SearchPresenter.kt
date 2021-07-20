package com.softwire.lner.trainboard.mobile.presenters

import com.softwire.lner.trainboard.mobile.AppDispatchersImpl
import com.softwire.lner.trainboard.mobile.contracts.SearchContract
import com.softwire.lner.trainboard.mobile.createAppTitle
import com.softwire.lner.trainboard.mobile.http.ApiClient
import com.softwire.lner.trainboard.mobile.http.StationsApiRequest
import com.softwire.lner.trainboard.mobile.models.Station
import com.softwire.lner.trainboard.mobile.models.StationCollection
import kotlinx.coroutines.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlin.coroutines.CoroutineContext

/**
 * The main presenter for the app. Acts as a controller for views and calls the main application
 * logic, most of which is contained within ./common.kt
 */
@UnstableDefault
class SearchPresenter: SearchContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private val job: Job = SupervisorJob()
    private val apiClient = ApiClient()
    private lateinit var view: SearchContract.View

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job
    private val coroutineScope = CoroutineScope(coroutineContext)

    private lateinit var stations: List<Station>

    /**
     * Views should call this when loaded.
     */
    @ImplicitReflectionSerializer
    override fun onViewTaken(view: SearchContract.View) {
        this.view = view
        view.setTitle(createAppTitle())

        coroutineScope.launch {
            withContext(dispatchers.io) {
                val response = apiClient.queryApi(StationsApiRequest())
                val stationCollection = response.collection
                val stations: List<Station>

                if (stationCollection is StationCollection) {
                    stations = stationCollection.stations

                    // enter UI thread to update spinner
                    withContext(dispatchers.main) {
                        // add all stations that don't have a null CRS identifier
                        // TODO: sort stations?
                        view.displayStations(stations.filter { it.crs != null })
                    }
                } else {
                    // the response did not contain a station collection
                    if (response.apiError != null) {
                        // the API returned an error
                        view.displayErrorMessage(response.apiError.error_description)
                    } else {
                        // something else happened
                        view.displayErrorMessage("Failed to get stations: response of incorrect format.")
                    }
                }
            }
        }
    }

    override fun filterStations(filter: String?, stations: List<Station>) {
        coroutineScope.launch {
            withContext(dispatchers.default) {
                var stationsToDisplay = stations
                if (filter != null){
                    stationsToDisplay = stations.filter { it.stationName.startsWith(filter)
                            || it.crs!!.startsWith(filter)
                            || it.nlc.startsWith(filter) }
                }
                withContext(dispatchers.main) {
                    if (stationsToDisplay.count() == 0) {
                        view.displayErrorMessage("No suitable stations found.")
                    }
                    view.displayStations(stationsToDisplay)
                }
            }
        }
    }
}
