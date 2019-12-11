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

    fun getEmails(type: FolderType, account: Account, callback: GetEmailsCallback)

    fun getEmailById(id: Long, type: FolderType, account: Account, callback: GetEmailCallback)

    fun send(account: Account, email: Email, saveSent: Boolean, callback: Callback)

    fun delete(id: Long, type: FolderType, account: Account, callback: Callback)
}