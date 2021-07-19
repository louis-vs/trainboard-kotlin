package com.softwire.lner.trainboard.mobile.models

import kotlinx.serialization.Serializable

@Serializable
class Station(val displayName: String = "", val name: String = "", val crs: String?, val nlc: String) {
    val stationName: String
        get() = if (displayName == "") name else displayName

    override fun toString(): String {
        return stationName
    }
}
