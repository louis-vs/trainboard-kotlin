package com.softwire.lner.trainboard.mobile.models

import kotlinx.serialization.Serializable

/**
 * Deserializes journey data returned from the API. The main property is a list of journeys.
 */
@Serializable
data class JourneyCollection (val outboundJourneys: List<Journey>) : Collection
