package com.fantaike.emailmanager.data.source

import com.fantaike.emailmanager.data.source.local.AccountLocalDataSource
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.source.remote.AccountRemoteDataSource
import com.fantaike.emailmanagerkt.utils.AppExecutors


class AccountRepository(
    private val mLocalDataSource: AccountLocalDataSource,
    private val mRemoteDataSource: AccountRemoteDataSource
) :
    AccountDataSource {
    override fun add(account: Account, callback: AccountDataSource.CallBack) {
        mRemoteDataSource.add(account, object : AccountDataSource.CallBack {
            override fun onSuccess() {
                mLocalDataSource.add(account, callback)
            }

            override fun onError(ex: String) {
                callback.onError(ex)
            }
        })


    }

    fun getAccount( callback: AccountDataSource.AccountCallback) {
        mLocalDataSource.getAccount(callback)
    }
    fun getAllAccount() {

    }

    companion object {
        private val INSTANCE: AccountRepository? = null
        fun getInstance(
            localDataSource: AccountLocalDataSource,
            remoteDataSource: AccountRemoteDataSource
        ) = INSTANCE ?: synchronized(AccountRepository::class.java) {
            INSTANCE ?: AccountRepository(localDataSource, remoteDataSource)
        }
    }
}