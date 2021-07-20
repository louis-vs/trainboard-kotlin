package com.softwire.lner.trainboard.mobile.http

import io.ktor.client.request.*

/**
 * Represents a request to the API, to enable the ApiClient to execute it.
 *
 * Subclasses define the actual implementation of the request. They specify the request structure,
 * abstracting the parameters so that they can more readably be implemented from elsewhere in the
 * code.
 */
abstract class ApiRequest(protected val path: String, val requestType: RequestType) {
    /**
     * Contains constants. This is the Kotlin equivalent of `public static final`
     */
    companion object {
        const val HOST_NAME = "mobile-api-softwire2.lner.co.uk"
        // const val HOST_NAME = "mobile-api-dev.lner.co.uk"
        const val FARES_PATH = "/v1/fares"
        const val STATIONS_PATH = "/v1/stations"
    }

    /**
     * Public function to create the HTTP request in the form of a builer, which can be used as an
     * argument to HttpClient().request()
     */
    abstract fun createHttpRequest() : HttpRequestBuilder
}
