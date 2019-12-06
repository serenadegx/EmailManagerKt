package com.fantaike.emailmanager.send

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.multifile.Utils
import com.example.multifile.XRMultiFile
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.data.source.EmailDataSource
import com.fantaike.emailmanager.data.source.EmailRepository
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.data.Event
import com.fantaike.emailmanagerkt.utils.FileUtils

class SendEmailViewModel(private val mRepository: EmailRepository) : ViewModel() {
    val items = MutableLiveData<List<Attachment>>().apply {
        value = emptyList()
    }

    val receiver = MutableLiveData<String>()
    val copy = MutableLiveData<String>()
    val secret = MutableLiveData<String>()
    val send = MutableLiveData<String>()
    val subject = MutableLiveData<String>()
    val content = MutableLiveData<String>()
    val snackBarText = MutableLiveData<String>()
    val sentEvent = MutableLiveData<Unit>()
    val loadingEvent = MutableLiveData<Event>()
    val saveEvent = MutableLiveData<Unit>()
    private lateinit var mAccount: Account

    fun start(account: Account) {
        mAccount = account
        mAccount?.run {
            send.value = this.account
        }
    }

    fun send(save2Sent: Boolean) {
        loadingEvent.value = Event(true, "正在发送...")
        val email = Email()
        send.value?.let { email.from = it }
        receiver.value?.let { email.to = it }
        copy.value?.let { email.cc = it }
        secret.value?.let { email.bcc = it }
        subject.value?.let { email.subject = it }
        content.value?.let { email.content = it }
        items.value?.let { email.attachments = it }
        mRepository.send(mAccount, email, true, object : EmailDataSource.Callback {
            override fun onSuccess() {
                loadingEvent.postValue(Event(false, ""))
                sentEvent.postValue(Unit)
            }

            override fun onError() {
                loadingEvent.postValue(Event(false, ""))
                snackBarText.postValue("发送失败")
            }
        })
    }

    fun save2Drafts() {

    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 715 && data != null) {
            val list = XRMultiFile.getSelectResult(data)
            val attachments = mutableListOf<Attachment>()
            for (str: String in list) {
                val attachment = Attachment(
                    str.substring(str.lastIndexOf("/") + 1),
                    str,
                    FileUtils.getSize(str),
                    FileUtils.getPrintSize(str)
                )
                attachments.add(attachment)
            }
            items.value = attachments
        }
    }
}
