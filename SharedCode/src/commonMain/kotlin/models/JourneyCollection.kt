package com.softwire.lner.trainboard.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class JourneyCollection (val outboundJourneys: List<Journey>) : Collection
