package com.fantaike.emailmanagerkt.detail

import com.fantaike.emailmanager.data.Email

interface DetailNavigator {
    fun showDeleteConfirmDialog()

    fun reply(email: Email)

    fun forward(email: Email)
}