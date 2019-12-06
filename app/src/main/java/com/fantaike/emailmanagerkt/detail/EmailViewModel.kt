package com.fantaike.emailmanager.detail

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.data.source.EmailDataSource
import com.fantaike.emailmanager.data.source.EmailRepository
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.Attachment

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
    private lateinit var mAccount: Account

    fun start(id: Long, account: Account) {
        mRepository.getEmailById(id, account, this)
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

    }

}