package com.fantaike.emailmanager.data.source

import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.FolderType

interface EmailDataSource {
    interface GetEmailsCallback {
        fun onEmailsLoaded(emails: List<Email>, type: FolderType)

        fun onDataNotAvailable()
    }

    interface GetEmailCallback {
        fun onEmailLoaded(email: Email)

        fun onDataNotAvailable()
    }

    interface Callback {
        fun onSuccess()
        fun onError()
    }

    interface DownloadCallback {
        fun onStart(index: Int)
        fun onProgress(index: Int, percent: Float)
        fun onFinish(index: Int)
        fun onError(index: Int)
    }

    fun getEmails(type: FolderType, account: Account, callback: GetEmailsCallback)

    fun getEmailById(id: Int, type: FolderType, account: Account, callback: GetEmailCallback)

    fun send(account: Account, email: Email, saveSent: Boolean, callback: Callback)

    fun reply(account: Account, email: Email, saveSent: Boolean, callback: Callback)

    fun forward(account: Account, email: Email, saveSent: Boolean, callback: Callback)

    fun delete(id: Int, type: FolderType, account: Account, callback: Callback)

    fun download(
        account: Account,
        type: FolderType,
        id: Long,
        index: Int,
        path: String,
        callback: DownloadCallback
    )

}