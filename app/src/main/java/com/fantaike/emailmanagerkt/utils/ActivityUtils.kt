package com.fantaike.emailmanagerkt.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

val AppCompatActivity.tag: String
    get() = "EmailManager"

fun <T : ViewModel> AppCompatActivity.obtainAccountViewModel(clazz: Class<T>) =
    ViewModelProviders.of(this, AccountViewModelFactory.getInstance(application)).get(clazz)

fun <T : ViewModel> AppCompatActivity.obtainViewModel(clazz: Class<T>) =
    ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(clazz)

fun AppCompatActivity.checkPermission(context: Context, permissions: Array<out String>): Boolean {
    var flag = true
    for (i in permissions) {
        if (ContextCompat.checkSelfPermission(context, i) != PackageManager.PERMISSION_GRANTED) {
            flag = !flag
            break
        }
    }
    return flag
}