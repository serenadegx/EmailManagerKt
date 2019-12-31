package com.fantaike.emailmanager.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.fantaike.emailmanagerkt.data.Attachment

@Entity(tableName = "emails")
class Email() : Comparable<Email>, Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @ColumnInfo(name = "email_id")
    var emailId: Int = 0
    var type: Int = 0
    @ColumnInfo(name = "read")
    var isRead: Boolean = false
    var subject: String = ""
    var personal: String? = null
    var from: String = ""
    var date: String = ""
    var to: String? = null
    var cc: String? = null
    var bcc: String? = null
    var content: String = ""
    @ColumnInfo(name = "has_attach")
    var hasAttach: Boolean = false
    @Ignore
    var attachments: List<Attachment> = mutableListOf()
    @Ignore
    var append: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        isRead = parcel.readByte() != 0.toByte()
        subject = parcel.readString()
        personal = parcel.readString()
        from = parcel.readString()
        date = parcel.readString()
        to = parcel.readString()
        cc = parcel.readString()
        bcc = parcel.readString()
        content = parcel.readString()
        hasAttach = parcel.readByte() != 0.toByte()
        append = parcel.readString()
    }

    override fun compareTo(other: Email): Int {
        return other.id.compareTo(this.id)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeByte(if (isRead) 1 else 0)
        parcel.writeString(subject)
        parcel.writeString(personal)
        parcel.writeString(from)
        parcel.writeString(date)
        parcel.writeString(to)
        parcel.writeString(cc)
        parcel.writeString(bcc)
        parcel.writeString(content)
        parcel.writeByte(if (hasAttach) 1 else 0)
        parcel.writeString(append)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Email> {
        override fun createFromParcel(parcel: Parcel): Email {
            return Email(parcel)
        }

        override fun newArray(size: Int): Array<Email?> {
            return arrayOfNulls(size)
        }
    }
}