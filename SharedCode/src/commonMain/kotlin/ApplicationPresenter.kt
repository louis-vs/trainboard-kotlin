package com.softwire.lner.trainboard.mobile

import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlin.coroutines.CoroutineContext

/**
 * The main presenter for the app. Acts as a controller for views and calls the main application
 * logic, most of which is contained within ./common.kt
 */
@UnstableDefault
class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private val job: Job = SupervisorJob()
    private lateinit var view: ApplicationContract.View

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job
    private val coroutineScope = CoroutineScope(coroutineContext)

    /**
     * Views should call this when loaded.
     */
    @ImplicitReflectionSerializer
    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setTitle(createAppTitle())
        coroutineScope.launch {
            withContext(dispatchers.io) {
                val stations = getStationsFromApi()
                withContext(dispatchers.main) {
                    view.setStations(stations.filter { it.crs != null })
                }
            }
        }
    }

    /**
     * Runs an API call which returns information about the fares between two stations.
     */
    @ImplicitReflectionSerializer
    override fun runSearch(from: Station, to: Station) {
        view.disableSearchButton()
        coroutineScope.launch {
            withContext(dispatchers.io) {
                val apiResponse = queryApi(from.crs!!, to.crs!!)
                withContext(dispatchers.main) {
                    if (apiResponse.apiError == null) {
                        if (apiResponse.journeyCollection != null && apiResponse.journeyCollection.outboundJourneys.count() > 0) {
                            view.displayJourneys(apiResponse.journeyCollection)
                        } else {
                            view.displayErrorMessage("No suitable trains found.")
                        }
                    } else {
                        view.displayErrorMessage(apiResponse.apiError.error_description)
                    }
                    view.enableSearchButton()
                }
            }
        }
    }
}

@Serializable
data class JourneyCollection(val outboundJourneys: List<Journey>)

@Serializable
class Journey(
        val journeyId: String,
        val departureTime: String,
        val arrivalTime: String,
        val originStation: Station,
        val destinationStation: Station,
        val isFastestJourney: Boolean,
        val journeyDurationInMinutes: Int,
        val primaryTrainOperator: Map<String, String>,
        val status: Status
) {
    val departureTimeFormatted: String
        get() = dateTimeTzToString(stringToDateTimeTz(departureTime))
    val arrivalTimeFormatted: String
        get() = dateTimeTzToString(stringToDateTimeTz(arrivalTime))
}

@Serializable
class Station(val displayName: String = "", val name: String = "", val crs: String?, val nlc: String) {
    val stationName: String
        get() = if (displayName == "") name else displayName

    override fun toString(): String {
        return stationName
    }
}

@Serializable(with = StatusSerializer::class)
enum class Status (val statusText: String){
    NORMAL("normal"),
    DELAYED("delayed"),
    CANCELLED("cancelled"),
    FULLY_RESERVED("full");

    val backgroundColor: Color
        get() = when (this) {
            NORMAL -> Color(0xff, 0x00, 0x88, 0x00)
            DELAYED -> Color(0xff, 0xff, 0xbb, 0x00)
            CANCELLED -> Color(0xff, 0xff, 0x00, 0x00)
            FULLY_RESERVED -> Color(0xff, 0x00, 0x00, 0x00)
        }

    val textColor: Color
        get() = when (this) {
            DELAYED -> Color(0xff, 0x00, 0x00, 0x00)
            else -> Color(0xff, 0xff, 0xff, 0xff)
        }
}

@Serializer(forClass = Status::class)
object StatusSerializer {
    override val descriptor: SerialDescriptor = StringDescriptor

    override fun deserialize(decoder: Decoder): Status {
        return Status.valueOf(decoder.decodeString().toUpperCase())
    }

    override fun serialize(encoder: Encoder, status: Status) {
        encoder.encodeString(status.name.toLowerCase())
    }
}

@Serializable
data class StationCollection(val stations: List<Station>)

@Serializable
data class ApiError(val error: String, val error_description: String)

data class ApiResponse(val journeyCollection: JourneyCollection?, val apiError: ApiError?)

data class Color(val alpha: Int, val red: Int, val green: Int, val blue: Int)