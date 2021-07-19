package com.softwire.lner.trainboard.mobile.http

import com.softwire.lner.trainboard.mobile.models.Collection
import com.softwire.lner.trainboard.mobile.models.JourneyCollection
import com.softwire.lner.trainboard.mobile.models.StationCollection
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse

class ApiClient {
    @ImplicitReflectionSerializer
    @UnstableDefault
    suspend fun queryApi(request: ApiRequest) : ApiResponse {
        val collection: Collection
        val apiError: ApiError
        var response: ApiResponse

        try {
            collection = createClient().use { client ->
                when (request.requestType) {
                    RequestType.GET_FARES -> client.request<JourneyCollection>(request.createHttpRequest())
                    RequestType.GET_STATIONS -> client.request<StationCollection>(request.createHttpRequest())
                }
            }
            response = ApiResponse(collection, null)
        } catch (e: ResponseException) {
            apiError = Json.parse(e.response.readText())
            response =  ApiResponse(null, apiError)
        }

        return response
    }

    @UnstableDefault
    private fun createClient() = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
    }
}