package com.fantaike.emailmanager.data.source

import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.data.source.local.AccountLocalDataSource
import com.fantaike.emailmanager.data.source.local.EmailLocalDataSource
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.FolderType
import com.fantaike.emailmanagerkt.data.source.remote.AccountRemoteDataSource
import com.fantaike.emailmanagerkt.data.source.remote.EmailRemoteDataSource
import com.fantaike.emailmanagerkt.utils.AppExecutors

class EmailRepository(
    private val mRemoteDataSource: EmailRemoteDataSource,
    private val mLocalDataSource: EmailLocalDataSource

) {
    var isCache: Boolean = true
    fun refresh() {
        isCache = false
    }

    fun getEmails(type: FolderType, account: Account, callback: EmailDataSource.GetEmailsCallback) {
        if (isCache) {
            mLocalDataSource.getEmails(type, account, object : EmailDataSource.GetEmailsCallback {
                override fun onEmailsLoaded(emails: List<Email>, type: FolderType) {
                    callback.onEmailsLoaded(emails, type)
                }

                override fun onDataNotAvailable() {
                    getEmailsFromRemoteDataSource(type, account, callback)
                }

            })
        } else {
            getEmailsFromRemoteDataSource(type, account, callback)
        }
    }

    fun getEmailById(id: Int, type: FolderType, account: Account, callback: EmailDataSource.GetEmailCallback) {
        mLocalDataSource.getEmail(id, type, account, object : EmailDataSource.GetEmailCallback {
            override fun onEmailLoaded(email: Email) {
                callback.onEmailLoaded(email)
            }

            override fun onDataNotAvailable() {
                mRemoteDataSource.getEmailById(id, type, account, callback)
            }

        })

    }

    fun send(account: Account, email: Email, saveSent: Boolean, callback: EmailDataSource.Callback) {
        mRemoteDataSource.send(account, email, saveSent, callback)
    }

    fun reply(account: Account, email: Email, saveSent: Boolean, callback: EmailDataSource.Callback) {
        mRemoteDataSource.reply(account, email, saveSent, callback)
    }

    fun forward(account: Account, email: Email, saveSent: Boolean, callback: EmailDataSource.Callback) {
        mRemoteDataSource.forward(account, email, saveSent, callback)
    }

    fun delete(id: Int, type: FolderType, account: Account, callback: EmailDataSource.Callback) {
        mRemoteDataSource.delete(id, type, account, callback)
    }

    fun download(
        account: Account,
        type: FolderType,
        id: Long,
        index: Int,
        path: String,
        callback: EmailDataSource.DownloadCallback
    ) {
        mRemoteDataSource.download(account, type, id, index, path, object : EmailDataSource.DownloadCallback {
            override fun onStart(index: Int) {
                callback.onStart(index)
            }

            override fun onProgress(index: Int, percent: Float) {
                callback.onProgress(index, percent)
            }

            override fun onFinish(index: Int) {
                callback.onFinish(index)
            }

            override fun onError(index: Int) {
                callback.onError(index)
            }
        })
    }

    fun getEmailsFromRemoteDataSource(type: FolderType, account: Account, callback: EmailDataSource.GetEmailsCallback) {
        mRemoteDataSource.getEmails(type, account, object : EmailDataSource.GetEmailsCallback {
            override fun onEmailsLoaded(emails: List<Email>, type: FolderType) {
                refreshLocalDataSource(type, emails)
                callback.onEmailsLoaded(emails, type)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

        })
    }

    private fun refreshLocalDataSource(type: FolderType, emails: List<Email>) {
        mLocalDataSource.deleteByType(type)
        mLocalDataSource.save(emails)
        isCache = true
    }

    companion object {
        private val INSTANCE: EmailRepository? = null
        fun getInstance(
            localDataSource: EmailLocalDataSource,
            remoteDataSource: EmailRemoteDataSource
        ) = INSTANCE ?: synchronized(EmailRepository::class.java) {
            INSTANCE ?: EmailRepository(remoteDataSource, localDataSource)
        }
    }
}