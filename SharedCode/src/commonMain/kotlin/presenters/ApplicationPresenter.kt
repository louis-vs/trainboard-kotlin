package com.softwire.lner.trainboard.mobile.presenters

import com.softwire.lner.trainboard.mobile.AppDispatchersImpl
import com.softwire.lner.trainboard.mobile.contracts.ApplicationContract
import com.softwire.lner.trainboard.mobile.createAppTitle
import com.softwire.lner.trainboard.mobile.http.ApiClient
import com.softwire.lner.trainboard.mobile.http.FaresApiRequest
import com.softwire.lner.trainboard.mobile.http.StationsApiRequest
import com.softwire.lner.trainboard.mobile.models.JourneyCollection
import com.softwire.lner.trainboard.mobile.models.Station
import com.softwire.lner.trainboard.mobile.models.StationCollection
import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import kotlinx.coroutines.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlin.coroutines.CoroutineContext

/**
 * The main presenter for the app. Acts as a controller for views and calls the main application
 * logic, most of which is contained within ./common.kt
 */
@UnstableDefault
class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private val job: Job = SupervisorJob()
    private val apiClient = ApiClient()
    private lateinit var view: ApplicationContract.View

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job
    private val coroutineScope = CoroutineScope(coroutineContext)

    /**
     * Views should call this when loaded.
     */
    @ImplicitReflectionSerializer
    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setTitle(createAppTitle())

        // set stations in coroutine
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
                        view.setStations(stations.filter { it.crs != null })
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

    /**
     * Runs an API call which returns information about the fares between two stations.
     */
    @ImplicitReflectionSerializer
    override fun runSearch(from: Station, to: Station) {
        // disable the search button to avoid sending too many requests
        view.disableSearchButton()

        coroutineScope.launch {
            // enter IO thread to send request
            withContext(dispatchers.io) {
                // make request
                val currentTime = DateTime.now() + TimeSpan(60000.0)
                val apiRequest = FaresApiRequest(from.crs!!, to.crs!!, currentTime)
                val apiResponse = apiClient.queryApi(apiRequest)

                // enter UI thread to update the view
                withContext(dispatchers.main) {
                    if (apiResponse.apiError == null) {
                        if (apiResponse.collection != null && apiResponse.collection is JourneyCollection && apiResponse.collection.outboundJourneys.count() > 0) {
                            view.displayJourneys(apiResponse.collection)
                        } else {
                            // this error message isn't very descriptive - a number of things could have happened here
                            view.displayErrorMessage("No suitable trains found.")
                        }
                    } else {
                        view.displayErrorMessage(apiResponse.apiError.error_description)
                    }

                    // re-enable the search button
                    view.enableSearchButton()
                }
            }
        }
    }
}
