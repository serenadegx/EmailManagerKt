package com.fantaike.emailmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config")
class Configuration constructor(
    categoryId: Long, name: String,
    receiveProtocol: String,
    receiveHostKey: String, receiveHostValue: String, receivePortKey: String,
    receivePortValue: String, receiveEncryptKey: String, receiveEncryptValue: Boolean,
    sendProtocol: String, sendHostKey: String, sendHostValue: String, sendPortKey: String,
    sendPortValue: String, sendEncryptKey: String, sendEncryptValue: Boolean,
    authKey: String, authValue: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var categoryId: Long = categoryId
    var name: String = name
    var receiveProtocol: String = receiveProtocol
    var receiveHostKey: String = receiveHostKey
    var receiveHostValue: String = receiveHostValue
    var receivePortKey: String = receivePortKey
    var receivePortValue: String = receivePortValue
    var receiveEncryptKey: String = receiveEncryptKey
    var receiveEncryptValue: Boolean = receiveEncryptValue
    var sendProtocol: String = sendProtocol
    var sendHostKey: String = sendHostKey
    var sendHostValue: String = sendHostValue
    var sendPortKey: String = sendPortKey
    var sendPortValue: String = sendPortValue
    var sendEncryptKey: String = sendEncryptKey
    var sendEncryptValue: Boolean = sendEncryptValue
    var authKey: String = authKey
    var authValue: Boolean = authValue


}