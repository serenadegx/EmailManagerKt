package com.fantaike.emailmanagerkt

import android.app.Application
import android.os.StrictMode
import android.provider.Settings
import com.fantaike.emailmanager.data.Configuration
import com.fantaike.emailmanagerkt.data.Account

@Suppress("DEPRECATION")
class EmailApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //性能测试(严苛模式)
//        if (isDevelopMode()) {
//            StrictMode.setThreadPolicy(
//                StrictMode.ThreadPolicy.Builder()
//                    .detectNetwork()
//                    .detectDiskWrites()
//                    .detectDiskReads()
//                    .penaltyDialog()
//                    .penaltyLog()
//                    .build()
//            )
//            StrictMode.setVmPolicy(
//                StrictMode.VmPolicy.Builder()
//                    .detectActivityLeaks()
//                    .detectLeakedRegistrationObjects()
////                .setClassInstanceLimit()
//                    .detectLeakedClosableObjects()
//                    .build()
//            )
//        }
    }

    private fun isDevelopMode(): Boolean {
        return Settings.Secure.getInt(contentResolver, Settings.Secure.ADB_ENABLED, 0) > 0
    }

    companion object {
        var configs: List<Configuration>? = null
        var account: Account? = null
    }
}