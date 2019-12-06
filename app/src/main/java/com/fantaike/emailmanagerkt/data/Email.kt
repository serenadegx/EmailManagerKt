package com.fantaike.emailmanager.data

import com.fantaike.emailmanagerkt.data.Attachment

class Email : Comparable<Email> {
    var id: Long = 0
    var isRead: Boolean = false
    var subject: String = ""
    var personal: String? = null
    var from: String = ""
    var date: String = ""
    var to: String? = null
    var cc: String? = null
    var bcc: String? = null
    var content: String = ""
    var hasAttach: Boolean = false
    var attachments: List<Attachment> = mutableListOf()
    var append: String = ""
    override fun compareTo(other: Email): Int {
        return other.id.compareTo(this.id)
    }
}