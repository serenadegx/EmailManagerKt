package com.fantaike.emailmanagerkt.detail

import com.fantaike.emailmanager.data.Email

interface DetailNavigator {
    fun showDeleteConfirmDialog()

    fun reply(email: Email)

    fun forward(email: Email)

    fun onStart(index: Int)

    fun onProgress(index: Int, percent: Float)

    fun onFinish(index: Int)

    fun onError(index: Int)
}