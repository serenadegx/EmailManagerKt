package com.fantaike.emailmanagerkt.send

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.detail.adapter.AttachmentListAdapter

@BindingAdapter("android:items")
fun RecyclerView.setItems(data: List<Attachment>) {
    (adapter as AttachmentListAdapter).setNewData(data)
}