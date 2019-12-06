package com.fantaike.emailmanagerkt.data

import com.fantaike.emailmanager.data.Configuration

class Account(
    id: Long,
    configId: Long,
    isCur: Boolean
) {
    var id: Long = id;
    var account: String? = "";
    var pwd: String? = "";
    var configId: Long = configId;
    var config: Configuration? = null;
    var isCur: Boolean = isCur;
    var personal: String = "";
    var remark: String = "";
}