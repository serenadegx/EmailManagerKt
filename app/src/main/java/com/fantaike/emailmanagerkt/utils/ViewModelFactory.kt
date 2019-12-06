package com.fantaike.emailmanagerkt.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fantaike.emailmanager.account.VerifyViewModel
import com.fantaike.emailmanager.data.source.AccountRepository
import com.fantaike.emailmanager.data.source.EmailRepository
import com.fantaike.emailmanager.detail.EmailViewModel
import com.fantaike.emailmanager.send.SendEmailViewModel
import com.fantaike.emailmanagerkt.emails.EmailsViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(clazz: Class<T>): T =
        with(clazz) {
            when {
                this.isAssignableFrom(VerifyViewModel::class.java) ->
                    VerifyViewModel(AccountRepository.getInstance())
                this.isAssignableFrom(EmailsViewModel::class.java) ->
                    EmailsViewModel(EmailRepository.getInstance())
                this.isAssignableFrom(EmailViewModel::class.java) ->
                    EmailViewModel(EmailRepository.getInstance())
                this.isAssignableFrom(SendEmailViewModel::class.java) ->
                    SendEmailViewModel(EmailRepository.getInstance())
                else -> throw  IllegalArgumentException("Unknown ViewModel class ${clazz.name}")
            }
        } as T

    companion object {
        private val INSTANCE: ViewModelFactory? = null
        fun getInstance() = INSTANCE ?: synchronized(ViewModelFactory::class.java) {
            INSTANCE ?: ViewModelFactory()
        }
    }
}