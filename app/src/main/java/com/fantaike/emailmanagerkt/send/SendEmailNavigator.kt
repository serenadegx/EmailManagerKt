package com.fantaike.emailmanager.send

interface SendEmailNavigator {
    fun onSending(msg: String)
    fun onSent()
    fun onSaving(msg: String)
    fun onSaved()
    fun onError(isSend: Boolean)
}