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
        coroutineScope.launch {
            withContext(dispatchers.io) {
                val stationCollection = apiClient.queryApi(StationsApiRequest()).collection
                val stations: List<Station>
                if (stationCollection is StationCollection) {
                    stations = stationCollection.stations
                    withContext(dispatchers.main) {
                        view.setStations(stations.filter { it.crs != null })
                    }
                } else {
                    // smartcast failed
                    view.displayErrorMessage("Failed to get stations: response of incorrect format.")
                }
            }
        }
    }

    /**
     * Runs an API call which returns information about the fares between two stations.
     */
    @ImplicitReflectionSerializer
    override fun runSearch(from: Station, to: Station) {
        view.disableSearchButton()
        coroutineScope.launch {
            withContext(dispatchers.io) {
                val currentTime = DateTime.now() + TimeSpan(60000.0)
                val apiRequest = FaresApiRequest(from.crs!!, to.crs!!, currentTime)
                val apiResponse = apiClient.queryApi(apiRequest)
                withContext(dispatchers.main) {
                    if (apiResponse.apiError == null) {
                        if (apiResponse.collection != null && apiResponse.collection is JourneyCollection && apiResponse.collection.outboundJourneys.count() > 0) {
                            view.displayJourneys(apiResponse.collection)
                        } else {
                            view.displayErrorMessage("No suitable trains found.")
                        }
                    } else {
                        view.displayErrorMessage(apiResponse.apiError.error_description)
                    }
                    view.enableSearchButton()
                }
            }
        }
    }
}
