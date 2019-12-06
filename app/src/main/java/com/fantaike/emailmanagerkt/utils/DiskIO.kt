package com.fantaike.emailmanagerkt.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DiskIOExecutor : Executor {
    private val mExecutor = Executors.newSingleThreadExecutor()
    override fun execute(command: Runnable?) {
        mExecutor.execute(command)
    }

}
