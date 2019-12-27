package com.fantaike.emailmanagerkt.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fantaike.emailmanager.account.VerifyViewModel
import com.fantaike.emailmanager.data.source.AccountRepository
import com.fantaike.emailmanager.data.source.EmailRepository
import com.fantaike.emailmanager.data.source.local.AccountLocalDataSource
import com.fantaike.emailmanager.data.source.local.EmailLocalDataSource
import com.fantaike.emailmanager.detail.EmailViewModel
import com.fantaike.emailmanager.send.SendEmailViewModel
import com.fantaike.emailmanagerkt.data.source.local.EmailDataBase
import com.fantaike.emailmanagerkt.data.source.remote.AccountRemoteDataSource
import com.fantaike.emailmanagerkt.data.source.remote.EmailRemoteDataSource
import com.fantaike.emailmanagerkt.emails.EmailsViewModel
import java.lang.IllegalArgumentException

private val mAppExecutors: AppExecutors = AppExecutors()

class ViewModelFactory private constructor(private val mRepository: EmailRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(clazz: Class<T>): T =
        with(clazz) {
            when {
                this.isAssignableFrom(EmailsViewModel::class.java) ->
                    EmailsViewModel(mRepository)
                this.isAssignableFrom(EmailViewModel::class.java) ->
                    EmailViewModel(mRepository)
                this.isAssignableFrom(SendEmailViewModel::class.java) ->
                    SendEmailViewModel(mRepository)
                else -> throw  IllegalArgumentException("Unknown ViewModel class ${clazz.name}")
            }
        } as T

    companion object {
        private val INSTANCE: ViewModelFactory? = null
        fun getInstance(application: Application) = INSTANCE ?: synchronized(ViewModelFactory::class.java) {
            INSTANCE ?: ViewModelFactory(
                EmailRepository.getInstance(
                    EmailLocalDataSource.getInstance(mAppExecutors, EmailDataBase.getInstance(application).emailDao()),
                    EmailRemoteDataSource.getInstance(mAppExecutors)
                )
            )
        }
    }
}

class AccountViewModelFactory private constructor(private val mRepository: AccountRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(clazz: Class<T>): T =
        with(clazz) {
            when {
                this.isAssignableFrom(VerifyViewModel::class.java) ->
                    VerifyViewModel(mRepository)
                else -> throw  IllegalArgumentException("Unknown ViewModel class ${clazz.name}")
            }
        } as T

    companion object {
        private val INSTANCE: AccountViewModelFactory? = null

        fun getInstance(application: Application) = INSTANCE ?: synchronized(AccountViewModelFactory::class.java) {
            INSTANCE ?: AccountViewModelFactory(
                AccountRepository.getInstance(
                    AccountLocalDataSource.getInstance(
                        mAppExecutors, EmailDataBase.getInstance(application).accountDao()
                    ), AccountRemoteDataSource.getInstance(
                        mAppExecutors
                    )
                )
            )
        }
    }

}