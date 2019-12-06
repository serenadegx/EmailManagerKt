package com.fantaike.emailmanagerkt.emails

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanagerkt.emails.adapter.EmailsListAdapter

@BindingAdapter("android:onRefresh")
fun SwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
    viewModel: EmailsViewModel
) {
    setOnRefreshListener { viewModel.refresh() }
}

@BindingAdapter("android:items")
fun RecyclerView.setItems(
    data: List<Email>
) {
    (adapter as EmailsListAdapter).setNewData(data)
}