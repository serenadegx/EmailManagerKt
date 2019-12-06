package com.fantaike.emailmanagerkt.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

fun <T : ViewModel> AppCompatActivity.obtainViewModel(clazz: Class<T>) =
    ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(clazz)