package com.fantaike.emailmanager.detail

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.data.source.EmailDataSource
import com.fantaike.emailmanager.data.source.EmailRepository
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.data.Event
import com.fantaike.emailmanagerkt.data.FolderType

class EmailViewModel(private val mRepository: EmailRepository) : ViewModel(), EmailDataSource.GetEmailCallback {
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

    fun start(id: Long, type: FolderType, account: Account) {
        this.id = id
        this.type = type
        mRepository.getEmailById(id, type, account, this)
    }

    override fun onEmailLoaded(email: Email) {
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

    }

    fun forward(view: View) {

    }

    fun delete(view: View) {
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

}