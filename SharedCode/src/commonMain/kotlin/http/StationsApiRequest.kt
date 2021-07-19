package com.softwire.lner.trainboard.mobile.http

import com.softwire.lner.trainboard.mobile.models.StationCollection
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * A request to the stations API.
 */
class StationsApiRequest : ApiRequest(STATIONS_PATH, RequestType.GET_STATIONS) {
    override fun createHttpRequest(): HttpRequestBuilder {
        val request = HttpRequestBuilder {
            protocol = URLProtocol.HTTPS
            host = HOST_NAME
            encodedPath = path
        }

        request.method = HttpMethod("GET")

        return request
    }
}