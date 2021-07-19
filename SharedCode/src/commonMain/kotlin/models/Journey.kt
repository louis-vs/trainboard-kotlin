package com.softwire.lner.trainboard.mobile.models

import com.softwire.lner.trainboard.mobile.dateTimeTzToString
import com.softwire.lner.trainboard.mobile.stringToDateTimeTz
import com.softwire.lner.trainboard.mobile.extraDay
import kotlinx.serialization.Serializable

/**
 * Holds information about a single journey as returned by the API.
 */
@Serializable
class Journey(
        val journeyId: String,
        val departureTime: String,
        val arrivalTime: String,
        val originStation: Station,
        val destinationStation: Station,
        val isFastestJourney: Boolean,
        val journeyDurationInMinutes: Int,
        val primaryTrainOperator: Map<String, String>,
        val status: Status
) {
    val departureTimeFormatted: String
        get() = dateTimeTzToString(stringToDateTimeTz(departureTime))
    val arrivalTimeFormatted: String
        get() = dateTimeTzToString(stringToDateTimeTz(arrivalTime))
    val extraDay: String
        get() = extraDay(stringToDateTimeTz(arrivalTime))
}
