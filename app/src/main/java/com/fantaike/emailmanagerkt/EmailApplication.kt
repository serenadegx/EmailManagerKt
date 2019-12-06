package com.fantaike.emailmanagerkt

import android.app.Application
import com.fantaike.emailmanager.data.Configuration
import com.fantaike.emailmanagerkt.data.Account

class EmailApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        var configs: List<Configuration>? = null
        var account: Account? = null
    }
}