package com.softwire.lner.trainboard.mobile

import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}

expect class AppDispatchersImpl(): AppDispatchers
