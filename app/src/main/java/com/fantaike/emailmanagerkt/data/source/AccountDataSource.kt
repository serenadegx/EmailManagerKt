package com.fantaike.emailmanager.data.source

import com.fantaike.emailmanagerkt.data.Account


interface AccountDataSource {
    interface CallBack {
        fun onSuccess()
        fun onError(ex: String)
    }

    interface AccountCallback {
        fun onAccountLoaded(account: Account)
        fun onDataNotAvailable()
    }

    interface AccountsCallback {
        fun onAccountsLoaded(accounts: List<Account>)
        fun onDataNotAvailable()
    }

    fun add(account: Account, callback: CallBack)

}