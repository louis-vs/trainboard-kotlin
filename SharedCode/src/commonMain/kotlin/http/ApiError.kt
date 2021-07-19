package com.softwire.lner.trainboard.mobile.http

import kotlinx.serialization.Serializable

/**
 * Deserializes errors returned from the API.
 */
@Serializable
data class ApiError(val error: String, val error_description: String)
