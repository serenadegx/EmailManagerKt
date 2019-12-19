package com.fantaike.emailmanager.data.source.local

import com.fantaike.emailmanager.data.source.AccountDataSource
import com.fantaike.emailmanagerkt.data.Account
import com.fantaike.emailmanagerkt.data.source.local.AccountDao
import com.fantaike.emailmanagerkt.utils.AppExecutors

class AccountLocalDataSource private constructor(
    private val mAppExecutors: AppExecutors,
    private val accountDao: AccountDao
) :
    AccountDataSource {
    override fun add(account: Account, callback: AccountDataSource.CallBack) {
        mAppExecutors.diskIO.execute {
            accountDao.insert(account)
            callback.onSuccess()
        }
    }

    fun getAccount(callback: AccountDataSource.AccountCallback) {
        mAppExecutors.diskIO.execute {
            val account = accountDao.queryCur()
            if (account != null) {
                callback.onAccountLoaded(account)
            } else {
                callback.onDataNotAvailable()
            }

        }
    }

    fun getAllAccount(callback: AccountDataSource.AccountsCallback) {
        mAppExecutors.diskIO.execute {
            callback.onAccountsLoaded(accountDao.queryAll())
        }
    }

    fun delete(account: Account, callback: AccountDataSource.CallBack) {
        mAppExecutors.diskIO.execute {
            accountDao.delete(account)
        }

    }

    fun update(account: Account, callback: AccountDataSource.CallBack) {
        mAppExecutors.diskIO.execute {
            val last = accountDao.queryCur()
            accountDao.update(last)
            accountDao.update(account)
        }
    }

    companion object {
        private var INSTANCE: AccountLocalDataSource? = null

        fun getInstance(appExecutors: AppExecutors, accountDao: AccountDao): AccountLocalDataSource {
            if (INSTANCE == null) {
                synchronized(AccountLocalDataSource::class.java) {
                    INSTANCE = AccountLocalDataSource(appExecutors, accountDao)
                }
            }
            return INSTANCE!!
        }
    }
}