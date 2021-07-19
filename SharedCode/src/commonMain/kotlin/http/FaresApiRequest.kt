package com.softwire.lner.trainboard.mobile.http

import com.soywiz.klock.DateTime
import io.ktor.client.request.*
import io.ktor.http.*

class FaresApiRequest(private val fromStation: String, private val toStation: String, private val outboundDateTime: DateTime) : ApiRequest(FARES_PATH, RequestType.GET_FARES) {
    override fun createHttpRequest() : HttpRequestBuilder {
        val request = HttpRequestBuilder {
            protocol = URLProtocol.HTTPS
            host = HOST_NAME
            encodedPath = path
        }

        request.method = HttpMethod("GET")

        request.parameter("originStation", fromStation)
        request.parameter("destinationStation", toStation)
        request.parameter("numberOfAdults", 1)
        request.parameter("numberOfChildren", 0)
        request.parameter("outboundDateTime", outboundDateTime.format("YYYY-MM-ddTHH:mm:ssXXX"))

        return request
    }
}