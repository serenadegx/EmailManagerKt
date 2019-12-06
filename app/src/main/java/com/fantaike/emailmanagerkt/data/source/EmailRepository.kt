package com.fantaike.emailmanager.data.source

import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.data.source.local.EmailLocalDataSource
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.source.remote.EmailRemoteDataSource
import com.fantaike.emailmanagerkt.utils.AppExecutors
import com.fantaike.emailmanagerkt.utils.ViewModelFactory

class EmailRepository(
    private val mRemoteDataSource: EmailRemoteDataSource,
    private val mLocalDataSource: EmailLocalDataSource,
    private val mAppExecutors: AppExecutors

) {
    var isCache: Boolean = true
    fun refresh() {
        isCache = false
    }

    fun getEmails(type: Int, account: Account, callback: EmailDataSource.GetEmailsCallback) {
        mAppExecutors.networkIO.execute {
            mRemoteDataSource.getEmails(type, account, callback)
        }
    }

    fun getEmailById(id: Long, account: Account, callback: EmailDataSource.GetEmailCallback) {
        mAppExecutors.networkIO.execute {
            mRemoteDataSource.getEmailById(id, account, callback)
        }
    }

    fun send(account: Account, email: Email, saveSent: Boolean, callback: EmailDataSource.Callback) {
        mAppExecutors.networkIO.execute {
            mRemoteDataSource.send(account, email, saveSent, callback)
        }
    }

    companion object {
        private val INSTANCE: EmailRepository? = null
        fun getInstance() = INSTANCE ?: synchronized(EmailRepository::class.java) {
            INSTANCE ?: EmailRepository(EmailRemoteDataSource.getInstance(), EmailLocalDataSource(), AppExecutors())
        }
    }
}