package com.fantaike.emailmanager.detail

import android.app.NotificationManager
import android.content.Context
import android.os.Environment
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.data.source.EmailDataSource
import com.fantaike.emailmanager.data.source.EmailRepository
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.data.Event
import com.fantaike.emailmanagerkt.data.FolderType
import com.fantaike.emailmanagerkt.detail.DetailNavigator
import java.io.File
import java.io.IOException

class EmailViewModel(private val mRepository: EmailRepository) : ViewModel(), EmailDataSource.GetEmailCallback {
    private var mEmail: Email? = null
    val items = MutableLiveData<List<Attachment>>().apply {
        value = emptyList()
    }
    val title = MutableLiveData<String>()

    val receivers = MutableLiveData<String>()
    val cc = MutableLiveData<String>()
    val bcc = MutableLiveData<String>()
    val subject = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val isAttach = MutableLiveData<Boolean>()
    val attachmentDes = MutableLiveData<String>()
    val html = MutableLiveData<String>()
    private val snackBarText = MutableLiveData<String>()
    val snackBarMessage: MutableLiveData<String>
        get() = snackBarText
    val saveEvent = MutableLiveData<Unit>()
    val deleteEvent = MutableLiveData<Unit>()
    val loadingEvent = MutableLiveData<Event>()
    private lateinit var mAccount: Account

    private var id = 0L
    private var type = FolderType.INBOX
    private lateinit var mNavigator: DetailNavigator

    fun start(id: Long, type: FolderType, account: Account, navigator: DetailNavigator) {
        this.id = id
        this.type = type
        mNavigator = navigator
        mAccount = account
        mRepository.getEmailById(id, type, mAccount, this)
    }

    override fun onEmailLoaded(email: Email) {
        this.mEmail = email
        title.postValue(email.personal ?: email.from)
        receivers.postValue(email.to)
        cc.postValue(email.cc?.also { "空" })
        bcc.postValue(email.cc?.also { "空" })
        subject.postValue(email.subject)
        date.postValue(email.date)
        isAttach.postValue(email.attachments.isNotEmpty())
        attachmentDes.postValue("${email.attachments.size}个附件")
        items.postValue(email.attachments)
        html.postValue(email.content)
    }

    override fun onDataNotAvailable() {
        snackBarText.postValue("获取邮件失败")
    }

    fun reply(view: View) {
        mEmail?.run {
            mNavigator.reply(this)
        }

    }

    fun forward(view: View) {
        mEmail?.run {
            mNavigator.forward(this)
        }
    }

    fun delete(view: View) {
        mNavigator.showDeleteConfirmDialog()
    }

    fun deleteEmail() {
        loadingEvent.postValue(Event(true, "正在删除..."))
        mRepository.delete(id, type, mAccount, object : EmailDataSource.Callback {
            override fun onSuccess() {
                loadingEvent.postValue(Event(false, ""))
                deleteEvent.postValue(Unit)
            }

            override fun onError() {
                loadingEvent.postValue(Event(false, ""))
                snackBarText.postValue("删除失败")
            }
        })
    }

    fun download(type: FolderType, id: Long, index: Int, path: String, fileName: String) {
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdir()
        }
        val file = File(dir, fileName)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        mRepository.download(mAccount, type, id, index, file.absolutePath, object : EmailDataSource.DownloadCallback {
            override fun onStart(index: Int) {
                mNavigator.onStart(index)
            }

            override fun onProgress(index: Int, percent: Float) {
                mNavigator.onProgress(index, percent)
            }

            override fun onFinish(index: Int) {
                mNavigator.onFinish(index)
            }

            override fun onError(index: Int) {
                mNavigator.onError(index)
            }
        })
    }

}