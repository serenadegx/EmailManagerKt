package com.fantaike.emailmanagerkt

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.fantaike.emailmanager.account.EmailCategoryActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            EmailCategoryActivity.start2EmailCategoryActivity(this)
        }, 1000);
    }
}