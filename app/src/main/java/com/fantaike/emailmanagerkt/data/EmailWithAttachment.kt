package com.fantaike.emailmanagerkt.data

import androidx.room.Embedded
import androidx.room.Relation
import com.fantaike.emailmanager.data.Email

class EmailWithAttachment {
    @Embedded
    var email: Email? = null
    @Relation(parentColumn = "id", entityColumn = "emailId")
    var attachments: List<Attachment>? = null
}