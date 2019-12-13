package com.fantaike.emailmanagerkt.detail

import android.webkit.WebView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.detail.adapter.AttachmentListAdapter

@BindingAdapter("android:attachments")
fun RecyclerView.setItems(data: List<Attachment>) {
    (adapter as AttachmentListAdapter).setNewData(data)
}

@BindingAdapter("android:html")
fun WebView.setHtml(html: String?) {
    this.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
}