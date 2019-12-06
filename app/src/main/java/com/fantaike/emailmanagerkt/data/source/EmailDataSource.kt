package com.fantaike.emailmanager.data.source

import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanagerkt.data.Account

interface EmailDataSource {
    interface GetEmailsCallback {
        fun onEmailsLoaded(emails: List<Email>)

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

    fun getEmails(type: Int, account: Account, callback: GetEmailsCallback)

    fun getEmailById(id: Long, account: Account, callback: GetEmailCallback)

    fun send(account: Account, email: Email, saveSent: Boolean, callback: Callback)
}