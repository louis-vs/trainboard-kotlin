package com.softwire.lner.trainboard.mobile.contracts

import com.softwire.lner.trainboard.mobile.models.JourneyCollection
import com.softwire.lner.trainboard.mobile.models.Station
import kotlinx.coroutines.CoroutineScope

/**
 * This contract specifies what methods the Main view must implement, and what methods the Main
 * presenter exposes to the view.
 */
interface ApplicationContract {
    interface View {
        /**
         * Sets the heading and subheading for the window.
         */
        fun setTitle(title: String)

        /**
         * Saves a list of Station objects as private variable.
         */
        fun saveStations(stations: List<Station>)

        /**
         * Launches activity for station selection.
         */
        fun launchSearchActivity(title: String)

        /**
         * Displays an error message to the user
         */
        fun displayErrorMessage(message: String)

        fun displayJourneys(journeyCollection: JourneyCollection)

        fun enableSearchButton()
        fun disableSearchButton()

        /**
         * [RETIRED: Reference Only] Sets the contents of the spinners/pickers to a list of Station objects.
         */
        fun setStations(stations: List<Station>)

        /**
         * [RETIRED: Reference Only] Opens a URL in the browser.
         */
        fun openUrl(url: String)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun runSearch(from: Station?, to: Station?)
    }
}
