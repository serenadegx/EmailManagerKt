package com.fantaike.emailmanager.detail

interface EmailDetailNavigator {
    fun onStart(index: Int)
    fun onProgress(index: Int, percent: Float)
    fun onFinish(index: Int)
}