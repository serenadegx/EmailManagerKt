package com.fantaike.emailmanagerkt.data

import androidx.room.*
import com.fantaike.emailmanager.data.Configuration

@Entity(tableName = "accounts")
class Account(
    id: Long,
    configId: Long,
    isCur: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = id
    @ColumnInfo(name = "account")
    var account: String? = ""
    @ColumnInfo(name = "pwd")
    var pwd: String? = ""
    @ColumnInfo(name = "config_id")
    var configId: Long = configId
    @Embedded
    var config: Configuration? = null
    @ColumnInfo(name = "cur")
    var isCur: Boolean = isCur
    @ColumnInfo(name = "personal")
    var personal: String = ""
    @ColumnInfo(name = "remark")
    var remark: String = ""
}