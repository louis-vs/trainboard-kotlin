package com.softwire.lner.trainboard.mobile.presenters

import com.softwire.lner.trainboard.mobile.AppDispatchersImpl
import com.softwire.lner.trainboard.mobile.contracts.SearchContract
import com.softwire.lner.trainboard.mobile.createAppTitle
import com.softwire.lner.trainboard.mobile.models.Station
import kotlinx.coroutines.*
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
    private lateinit var view: SearchContract.View

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job
    private val coroutineScope = CoroutineScope(coroutineContext)

    /**
     * Views should call this when loaded.
     */
    override fun onViewTaken(view: SearchContract.View) {
        this.view = view
        view.setTitle(createAppTitle())
    }

    override fun filterStations(filter: String?, stations: List<Station>) {
        coroutineScope.launch {
            withContext(dispatchers.default) {
                var stationsToDisplay = stations.toList()
                if (filter != null){
                    stationsToDisplay = stations.filter { it.stationName.toLowerCase().startsWith(filter)
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
