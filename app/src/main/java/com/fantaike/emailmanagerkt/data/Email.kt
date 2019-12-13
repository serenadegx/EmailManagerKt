package com.fantaike.emailmanager.data

import android.os.Parcel
import android.os.Parcelable
import com.fantaike.emailmanagerkt.data.Attachment

class Email() : Comparable<Email>,Parcelable {
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