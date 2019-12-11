package com.fantaike.emailmanager.account

import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fantaike.emailmanager.data.source.AccountDataSource
import com.fantaike.emailmanager.data.source.AccountRepository
import com.fantaike.emailmanagerkt.EmailApplication
import com.fantaike.emailmanagerkt.data.Account

class VerifyViewModel(private val mRepository: AccountRepository) : ViewModel() {
    private var categoryId: Long = 0L
    val account = MutableLiveData<String>()
    val pwd = MutableLiveData<String>()

    private val snackBarText = MutableLiveData<String>()
    val snackBarMessage: MutableLiveData<String>
        get() = snackBarText
    private val isVerify = MutableLiveData<Boolean>()
    val verify: MutableLiveData<Boolean>
        get() = isVerify

    fun start(categoryId: Long) {
        this.categoryId = categoryId
        account.value = "guoxinrui@fantaike.ai"
        pwd.value = "1993Gxr"
    }

    /**
     * 添加邮箱账户
     */
    fun addAccount(view: View) {
        if (TextUtils.isEmpty(account.value) || TextUtils.isEmpty(pwd.value)) {
            return
        } else {
            var params = Account(1, categoryId, true)
            params.account = account.value
            params.pwd = pwd.value
            params.configId = categoryId
            EmailApplication.configs?.let {
                params.config = it[categoryId.toInt() - 1]
                mRepository.add(params, object : AccountDataSource.CallBack {
                    override fun onSuccess() {
                        snackBarText.postValue("登陆成功")
                        EmailApplication.account = params
                        SystemClock.sleep(1000)
                        isVerify.postValue(true)
                    }

                    override fun onError(ex: String) {
                        snackBarText.postValue(ex)
                    }
                })

            }
        }
    }

    /**
     * 保存账户至本地
     */
    private fun saveAccount() {

    }

}