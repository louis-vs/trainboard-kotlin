package com.softwire.lner.trainboard.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class StationCollection(val stations: List<Station>) : Collection
