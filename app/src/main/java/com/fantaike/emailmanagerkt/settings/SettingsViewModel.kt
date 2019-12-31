package com.fantaike.emailmanager.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fantaike.emailmanagerkt.data.Account

class SettingsViewModel : ViewModel() {
    val mItems = MutableLiveData<List<Account>>().apply {
        value = emptyList()
    }
    val personal = MutableLiveData<String>()
    val signature = MutableLiveData<String>()
    val isSave = MutableLiveData<Boolean>()
    val isNotify = MutableLiveData<Boolean>()

    val account = MutableLiveData<String>()
    val pwd = MutableLiveData<String>()
    val receiveHost = MutableLiveData<String>()
    val receivePort = MutableLiveData<String>()
    val isSSLRe = MutableLiveData<String>()
    val sendHost = MutableLiveData<String>()
    val sendPort = MutableLiveData<String>()
    val isSSLSe = MutableLiveData<String>()

    fun start(sp: SharedPreferences) {

    }

    fun loadConfig(){}

    fun editPersonal(){}

    fun editSignature(){}

    /**
     * 修改配置
     */
    fun modify(){}

    /**
     * 设置当前账号
     */
    fun setCur(){}

    fun delete(){}
}