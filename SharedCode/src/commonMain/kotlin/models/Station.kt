package com.softwire.lner.trainboard.mobile.models

import kotlinx.serialization.Serializable

/**
 * Holds information about a single station, as returned by both the stations API and the fares
 * API.
 *
 * The key for the human-readable name of a station varies between APIs - hence the default values.
 * To get the actual name of the station, use the stationName property. This is returned
 * automatically when toString() is used.
 */
@Serializable
class Station(val displayName: String = "", val name: String = "", val crs: String?, val nlc: String) {
    val stationName: String
        get() = if (displayName == "") name else displayName

    override fun toString(): String {
        return stationName
    }
}
