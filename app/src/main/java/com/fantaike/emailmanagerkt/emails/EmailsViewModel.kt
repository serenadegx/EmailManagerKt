package com.fantaike.emailmanagerkt.emails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.data.source.EmailDataSource
import com.fantaike.emailmanager.data.source.EmailRepository
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.FolderType

class EmailsViewModel(private val mRepository: EmailRepository) : ViewModel(), EmailDataSource.GetEmailsCallback {
    val items = MutableLiveData<List<Email>>().apply {
        //初始化
        value = emptyList()
    }

    val mDataLoading = MutableLiveData<Boolean>()
    val mEmpty: LiveData<Boolean> = Transformations.map(items) {
        it.isEmpty()
    }
    private lateinit var mAccount: Account
    private var mType: FolderType = FolderType.INBOX

    fun start(type: FolderType, account: Account) {
        mType = type
        mAccount = account
        loadEmails()
    }

    fun refresh() {
        mRepository.refresh()
        loadEmails()
    }

    private fun loadEmails() {
        mDataLoading.value = true
        mRepository.getEmails(mType, mAccount, this)
    }

    override fun onEmailsLoaded(emails: List<Email>, type: FolderType) {
        if (type == mType) {
            mDataLoading.postValue(false)
            items.postValue(emails.sortedByDescending { data -> data.id })
        }
    }

    override fun onDataNotAvailable() {
        mDataLoading.postValue(false)
    }


}
