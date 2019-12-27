package com.fantaike.emailmanagerkt.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "attachments")
data class Attachment @JvmOverloads constructor(
    val emailId: Long,
    val type: Int,
    val fileName: String,
    val path: String,
    val size: Long,
    val printSize: String
) {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    @Ignore
    var isDownload: Boolean = false
    @Ignore
    var enable: Boolean = true

}
