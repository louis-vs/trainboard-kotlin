package com.softwire.lner.trainboard.mobile.http

/**
 * Stores the different kinds of request that are available.
 *
 * This doesn't have to correspond 1-to-1 with the number of ApiRequest subclasses. Rather, these
 * subclasses should roughly correspond to RESTful resources, with the RequestType corresponding to
 * the RESTful route.
 */
enum class RequestType {
    GET_FARES, GET_STATIONS
}