package com.softwire.lner.trainboard.mobile.models

import kotlinx.serialization.Serializable

/**
 * Deserializes station data returned from the API. The main property is a list of stations.
 */
@Serializable
data class StationCollection(val stations: List<Station>) : Collection
