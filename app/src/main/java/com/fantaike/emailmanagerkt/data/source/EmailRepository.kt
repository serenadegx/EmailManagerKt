package com.fantaike.emailmanager.data.source

import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.data.source.local.EmailLocalDataSource
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.FolderType
import com.fantaike.emailmanagerkt.data.source.remote.EmailRemoteDataSource
import com.fantaike.emailmanagerkt.utils.AppExecutors

class EmailRepository(
    private val mRemoteDataSource: EmailRemoteDataSource,
    private val mLocalDataSource: EmailLocalDataSource,
    private val mAppExecutors: AppExecutors

) {
    var isCache: Boolean = true
    fun refresh() {
        isCache = false
    }

    fun getEmails(type: FolderType, account: Account, callback: EmailDataSource.GetEmailsCallback) {
        mAppExecutors.networkIO.execute {
            mRemoteDataSource.getEmails(type, account, callback)
        }
    }

    fun getEmailById(id: Long, type: FolderType, account: Account, callback: EmailDataSource.GetEmailCallback) {
        mAppExecutors.networkIO.execute {
            mRemoteDataSource.getEmailById(id, type, account, callback)
        }
    }

    fun send(account: Account, email: Email, saveSent: Boolean, callback: EmailDataSource.Callback) {
        mAppExecutors.networkIO.execute {
            mRemoteDataSource.send(account, email, saveSent, callback)
        }
    }

    fun reply(account: Account, email: Email, saveSent: Boolean, callback: EmailDataSource.Callback) {
        mAppExecutors.networkIO.execute {
            mRemoteDataSource.reply(account, email, saveSent, callback)
        }
    }

    fun forward(account: Account, email: Email, saveSent: Boolean, callback: EmailDataSource.Callback) {
        mAppExecutors.networkIO.execute {
            mRemoteDataSource.forward(account, email, saveSent, callback)
        }
    }

    fun delete(id: Long, type: FolderType, account: Account, callback: EmailDataSource.Callback) {
        mAppExecutors.networkIO.execute {
            mRemoteDataSource.delete(id, type, account, callback)
        }
    }

    fun download(
        account: Account,
        type: FolderType,
        id: Long,
        index: Int,
        path: String,
        callback: EmailDataSource.DownloadCallback
    ) {
        mAppExecutors.networkIO.execute {
            mRemoteDataSource.download(account, type, id, index, path, object :EmailDataSource.DownloadCallback{
                override fun onStart(index: Int) {
                    callback.onStart(index)
                }

                override fun onProgress(index: Int, percent: Float) {
                    callback.onProgress(index, percent)
                }

                override fun onFinish(index: Int) {
                    mAppExecutors.mainThread.execute {
                        callback.onFinish(index)
                    }
                }

                override fun onError(index: Int) {
                    callback.onError(index)
                }
            })
        }
    }

    companion object {
        private val INSTANCE: EmailRepository? = null
        fun getInstance() = INSTANCE ?: synchronized(EmailRepository::class.java) {
            INSTANCE ?: EmailRepository(EmailRemoteDataSource.getInstance(), EmailLocalDataSource(), AppExecutors())
        }
    }
}