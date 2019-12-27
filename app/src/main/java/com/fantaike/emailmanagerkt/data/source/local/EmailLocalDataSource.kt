package com.fantaike.emailmanager.data.source.local

import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.data.source.EmailDataSource
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.FolderType
import com.fantaike.emailmanagerkt.data.source.local.EmailWithAttachmentDao
import com.fantaike.emailmanagerkt.utils.AppExecutors

class EmailLocalDataSource private constructor(
    private val mAppExecutors: AppExecutors,
    private val mEmailDao: EmailWithAttachmentDao
) {

    fun getEmails(type: FolderType, account: Account, callback: EmailDataSource.GetEmailsCallback) {
        mAppExecutors.diskIO.execute {
            val emails = mutableListOf<Email>()
            val data = mEmailDao.getEmails(type.ordinal)
            if (data.isEmpty()) {
                callback.onDataNotAvailable()
            } else {
                for (emailWithAttach in data) {
                    val email = emailWithAttach.email
                    email?.run {
                        email.attachments = emailWithAttach.attachments!!
                        emails.add(email)
                    }

                }
                callback.onEmailsLoaded(emails, type)
            }
        }
    }

    fun getEmail(id: Long, type: FolderType, account: Account, callback: EmailDataSource.GetEmailCallback) {
        mAppExecutors.diskIO.execute {
            val data = mEmailDao.getEmailById(id, type.ordinal)
            if (data == null) {
                callback.onDataNotAvailable()
            } else {
                val email = data.email
                email?.run {
                    attachments = data.attachments!!
                    callback.onEmailLoaded(email)
                }
            }
        }
    }

    fun save(emails: List<Email>) {
        mAppExecutors.diskIO.execute {
            mEmailDao.insertEmails(emails)
            for (email in emails) {
                mEmailDao.insertAttachments(email.attachments)
            }
        }
    }

    fun deleteById(id: Long, type: FolderType, callback: EmailDataSource.Callback) {
        mAppExecutors.diskIO.execute {
            val data = mEmailDao.getEmailById(id, type.ordinal)
            data?.run {
                attachments?.let { mEmailDao.deleteAttachments(it) }
                email?.let { mEmailDao.deleteEmails(it) }
            }
            callback.onSuccess()
        }
    }

    fun deleteAll() {
        mAppExecutors.diskIO.execute {
            val data1 = mEmailDao.getEmails(FolderType.INBOX.ordinal)
            val data2 = mEmailDao.getEmails(FolderType.SENT.ordinal)
            val data3 = mEmailDao.getEmails(FolderType.DELETED.ordinal)
            val data4 = mEmailDao.getEmails(FolderType.DRAFTS.ordinal)

            data1?.forEach {
                it.attachments?.run { mEmailDao.deleteAttachments(this) }
                it.email?.run { mEmailDao.deleteEmails(this) }
            }
            data2?.forEach {
                it.attachments?.run { mEmailDao.deleteAttachments(this) }
                it.email?.run { mEmailDao.deleteEmails(this) }
            }
            data3?.forEach {
                it.attachments?.run { mEmailDao.deleteAttachments(this) }
                it.email?.run { mEmailDao.deleteEmails(this) }
            }
            data4?.forEach {
                it.attachments?.run { mEmailDao.deleteAttachments(this) }
                it.email?.run { mEmailDao.deleteEmails(this) }
            }

        }
    }

    companion object {
        private var INSTANCE: EmailLocalDataSource? = null

        fun getInstance(appExecutors: AppExecutors, emailDao: EmailWithAttachmentDao): EmailLocalDataSource {
            if (INSTANCE == null) {
                synchronized(EmailLocalDataSource::class.java) {
                    INSTANCE = EmailLocalDataSource(appExecutors, emailDao)
                }
            }
            return INSTANCE!!
        }
    }
}