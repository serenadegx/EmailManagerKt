package com.fantaike.emailmanagerkt.data.source.local

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fantaike.emailmanager.data.Configuration
import com.fantaike.emailmanagerkt.data.Account

@Database(entities = [Account::class, Configuration::class], version = 1)
abstract class EmailDataBase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    companion object {
        private var INSTANCE: EmailDataBase? = null
        fun getInstance(application: Application): EmailDataBase {
            synchronized(EmailDataBase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        application.applicationContext,
                        EmailDataBase::class.java,
                        "email_manager.db"
                    )
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}