package com.fantaike.emailmanagerkt.data

data class Attachment(val fileName: String, val path: String, val size: Long, val printSize: String) {
    var isDownload: Boolean = false
    var enable: Boolean = true

}
