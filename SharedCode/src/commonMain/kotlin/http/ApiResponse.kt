package com.softwire.lner.trainboard.mobile.http

import com.softwire.lner.trainboard.mobile.models.Collection

/**
 * Holds information about the API response - namely, whether it returned a collection or an error.
 */
data class ApiResponse(val collection: Collection?, val apiError: ApiError?)
