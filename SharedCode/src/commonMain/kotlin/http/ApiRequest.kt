package com.softwire.lner.trainboard.mobile.http

import io.ktor.client.request.*

abstract class ApiRequest(protected val path: String, val requestType: RequestType) {
    companion object {
        const val HOST_NAME = "mobile-api-softwire2.lner.co.uk"
        const val FARES_PATH = "/v1/fares"
        const val STATIONS_PATH = "/v1/stations"
    }

    abstract fun createHttpRequest() : HttpRequestBuilder
}
