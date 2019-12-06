package com.fantaike.emailmanager.data.source

import com.fantaike.emailmanagerkt.data.Account


interface AccountDataSource {
    interface CallBack {
        fun onSuccess()
        fun onError(ex: String)
    }

    fun add(account: Account, callback: CallBack)

}