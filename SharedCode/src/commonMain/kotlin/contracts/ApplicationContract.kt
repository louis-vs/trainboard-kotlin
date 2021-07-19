package com.softwire.lner.trainboard.mobile.contracts

import com.softwire.lner.trainboard.mobile.models.JourneyCollection
import com.softwire.lner.trainboard.mobile.models.Station
import kotlinx.coroutines.CoroutineScope

/**
 * This contract specifies what methods a view must implement, and what methods the presenter
 * exposes to the view.
 */
interface ApplicationContract {
    interface View {
        /**
         * Sets the heading and subheading for the window.
         */
        fun setTitle(title: String)

        /**
         * Sets the contents of the spinners/pickers to a list of station codes
         */
        fun setStations(stations: List<Station>)

        /**
         * Opens a URL in the browser.
         */
        fun openUrl(url: String)

        /**
         * Displays an error message to the user
         */
        fun displayErrorMessage(message: String)

        fun displayJourneys(journeyCollection: JourneyCollection)

        fun enableSearchButton()
        fun disableSearchButton()
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun runSearch(from: Station, to: Station)
    }
}
