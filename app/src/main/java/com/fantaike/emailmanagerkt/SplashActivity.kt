package com.fantaike.emailmanagerkt

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.fantaike.emailmanager.account.EmailCategoryActivity
import com.fantaike.emailmanager.data.source.AccountDataSource
import com.fantaike.emailmanager.data.source.AccountRepository
import com.fantaike.emailmanager.data.source.local.AccountLocalDataSource
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.source.local.EmailDataBase
import com.fantaike.emailmanagerkt.utils.AppExecutors

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Thread{
            SystemClock.sleep(1000)
            AccountLocalDataSource.getInstance(AppExecutors(),EmailDataBase.getInstance(application).accountDao()).getAccount(object :AccountDataSource.AccountCallback{
                override fun onAccountLoaded(account: Account) {
                    EmailApplication.account = account
                    MainActivity.start2MainActivity(this@SplashActivity)
                    finish()
                }

                override fun onDataNotAvailable() {
                    EmailCategoryActivity.start2EmailCategoryActivity(this@SplashActivity)
                    finish()
                }
            })
        }.start()
    }
}