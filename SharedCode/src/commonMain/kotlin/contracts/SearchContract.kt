package com.softwire.lner.trainboard.mobile.contracts

import com.softwire.lner.trainboard.mobile.models.JourneyCollection
import com.softwire.lner.trainboard.mobile.models.Station
import kotlinx.coroutines.CoroutineScope

/**
 * This contract specifies what methods the Search view must implement, and what methods the Search
 * presenter exposes to the view.
 */
interface SearchContract {
    interface View {
        /**
         * Sets the heading and subheading for the window.
         */
        fun setTitle(title: String)

        /**
         * Sets the contents of the spinners/pickers to a list of station codes
         */
        fun displayStations(stations: List<Station>)

        /**
         * Displays an error message to the user
         */
        fun displayErrorMessage(message: String)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun filterStations(filter: String, stations: List<Station>)
    }
}

