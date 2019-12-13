package com.fantaike.emailmanagerkt.send

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.send.adapter.AttachmentListAdapter
import com.fantaike.emailmanagerkt.send.component.RichEditor

@BindingAdapter("android:items")
fun RecyclerView.setItems(data: List<Attachment>) {
    (adapter as AttachmentListAdapter).setNewData(data)
}

@BindingAdapter("android:html")
fun RichEditor.setHtml(data: String) {
    html = data
}

@InverseBindingAdapter(attribute = "android:html", event = "valueAttrChanged")
fun RichEditor.getHtml(): String {
    return html
}

