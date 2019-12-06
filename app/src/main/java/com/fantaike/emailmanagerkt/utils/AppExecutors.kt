package com.fantaike.emailmanagerkt.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

const val THREAD_COUNT = 3

data class AppExecutors(
    val diskIO: Executor = DiskIOExecutor(),
    val networkIO: Executor = Executors.newFixedThreadPool(THREAD_COUNT),
    val mainThread: Executor = MainThreadExecutor()
)

class MainThreadExecutor : Executor {
    private val mainHandler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable?) {
        mainHandler.post(command)
    }

}
