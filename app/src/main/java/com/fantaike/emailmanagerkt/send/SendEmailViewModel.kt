package com.fantaike.emailmanager.send

import android.content.Intent
import android.text.Html
import android.util.Log
import android.widget.EditText
import androidx.core.text.HtmlCompat
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
import com.fantaike.emailmanagerkt.data.SendType
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

    private var mType: SendType = SendType.NORMAL
    private var mData: Email? = null
    private lateinit var mAccount: Account

    fun start(account: Account, email: Email?, type: SendType) {
        mAccount = account
        mType = type
        mData = email
        send.value = mAccount.account
        when (type) {
            SendType.NORMAL ->
                content.value = "\nfrom EmailManager"
            SendType.REPLY -> {
                email?.run {
                    this@SendEmailViewModel.subject.value = "回复:${this.subject}"
                    receiver.value = this.from
                    val sb = StringBuilder("<br>")
                    sb.append("<div style=\"line-height:1.5\">-------- 原始邮件 --------<br>")
                    sb.append("主题：${email.subject}<br>")
                    sb.append("发件人：${email.from}<br>")
                    email.to?.run {
                        sb.append("发件人：${email.to}<br>")
                    }
                    email.cc?.run {
                        sb.append("抄送人：${email.cc}<br>")
                    }
                    sb.append("发件时间：${email.date}<br></div><br>")
                    this@SendEmailViewModel.content.value = sb.toString() + content
                }
            }
            SendType.FORWARD -> {
                email?.run {
                    this@SendEmailViewModel.subject.value = "转发:${this.subject}"
                    val sb = StringBuilder("<br>")
                    sb.append("<div style=\"line-height:1.5\"><br><br>-------- 原始邮件 --------<br>")
                    sb.append("主题：${email.subject}<br>")
                    sb.append("发件人：${email.from}<br>")
                    email.to?.run {
                        sb.append("发件人：${email.to}<br>")
                    }
                    email.cc?.run {
                        sb.append("抄送人：${email.cc}<br>")
                    }
                    sb.append("发件时间：${email.date}<br></div><br>")
                    this@SendEmailViewModel.content.value = sb.toString() + content
//                    items.value = this.attachments
                }
            }
            SendType.EDIT -> {
                email?.run {
                    val to = email.to
                    receiver.value = to?.substring(0, to.lastIndexOf(";"))
                    this@SendEmailViewModel.subject.value = this.subject
                    this@SendEmailViewModel.content.value = this.content
//                    items.value = this.attachments
                }
            }
        }
    }

    fun send(save2Sent: Boolean) {
//        Log.i("mango", "邮件内容：${content.value}")
        loadingEvent.value = Event(true, "正在发送...")
        val email = Email()
        mData?.let { email.id = it.id }
        send.value?.let { email.from = it }
        receiver.value?.let { email.to = it }
        copy.value?.let { email.cc = it }
        secret.value?.let { email.bcc = it }
        subject.value?.let { email.subject = it }
        content.value?.let { email.content = it }
        items.value?.let { email.attachments = it }
        when (mType) {
            SendType.NORMAL -> send(email)
            SendType.REPLY -> reply(email)
            SendType.FORWARD -> forward(email)
            SendType.EDIT -> send(email)
        }

    }

    private fun forward(email: Email) {
        mRepository.forward(mAccount, email, true, object : EmailDataSource.Callback {
            override fun onSuccess() {
                loadingEvent.postValue(Event(false, ""))
                sentEvent.postValue(Unit)
            }

            override fun onError() {
                loadingEvent.postValue(Event(false, ""))
                snackBarText.postValue("转发失败")
            }
        })
    }

    private fun reply(email: Email) {
        mRepository.reply(mAccount, email, true, object : EmailDataSource.Callback {
            override fun onSuccess() {
                loadingEvent.postValue(Event(false, ""))
                sentEvent.postValue(Unit)
            }

            override fun onError() {
                loadingEvent.postValue(Event(false, ""))
                snackBarText.postValue("回复失败")
            }
        })
    }

    private fun send(email: Email) {
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
