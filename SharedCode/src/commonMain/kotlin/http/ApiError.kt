package com.softwire.lner.trainboard.mobile.http

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(val error: String, val error_description: String)
