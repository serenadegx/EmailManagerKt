package com.fantaike.emailmanagerkt.data.source.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fantaike.emailmanager.data.Configuration
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.Attachment

@Database(entities = [Account::class, Configuration::class, Email::class, Attachment::class], version = 1)
abstract class EmailDataBase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    abstract fun emailDao(): EmailWithAttachmentDao


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