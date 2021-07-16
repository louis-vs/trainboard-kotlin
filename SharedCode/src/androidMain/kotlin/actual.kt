package com.softwire.lner.trainboard.mobile

import kotlinx.coroutines.Dispatchers

actual fun platformName(): String {
    return "Android"
}

actual class AppDispatchersImpl: AppDispatchers {
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
}
