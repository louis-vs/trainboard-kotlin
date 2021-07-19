package com.softwire.lner.trainboard.mobile.http

import com.softwire.lner.trainboard.mobile.models.Collection

data class ApiResponse(val collection: Collection?, val apiError: ApiError?)
