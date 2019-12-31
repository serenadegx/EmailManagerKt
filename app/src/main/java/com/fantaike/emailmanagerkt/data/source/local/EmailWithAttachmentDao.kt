package com.fantaike.emailmanagerkt.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.data.EmailWithAttachment

@Dao
interface EmailWithAttachmentDao {
    @Query("SELECT * FROM emails WHERE type = :type")
    fun getEmails(type: Int): List<EmailWithAttachment>

    @Query("SELECT * FROM emails WHERE email_id = :id AND type = :type LIMIT 1")
    fun getEmailById(id: Int, type: Int): EmailWithAttachment?

    @Delete()
    fun deleteEmails(vararg email: Email)

    @Delete
    fun deleteAttachments(attachments: List<Attachment>)

    @Insert
    fun insertEmails(emails: List<Email>)

    @Insert
    fun insertAttachments(attachments: List<Attachment>)


}