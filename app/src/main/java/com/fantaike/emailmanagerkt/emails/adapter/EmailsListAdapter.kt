package com.fantaike.emailmanagerkt.emails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.detail.EmailDetailActivity
import com.fantaike.emailmanagerkt.BR
import com.fantaike.emailmanagerkt.data.FolderType
import com.fantaike.emailmanagerkt.databinding.ItemEmailBinding
import com.fantaike.emailmanagerkt.databinding.ItemSentBinding

class EmailsListAdapter(data: List<Email>, type: FolderType) : RecyclerView.Adapter<EmailsListAdapter.MyViewHolder>() {
    private var mData: List<Email> = data
    private var mType: FolderType = type
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context;
        val binding: ViewDataBinding
        when (mType) {
            FolderType.INBOX -> binding = ItemEmailBinding.inflate(LayoutInflater.from(mContext), parent, false)
            FolderType.SENT -> binding = ItemSentBinding.inflate(LayoutInflater.from(mContext), parent, false)
            FolderType.DRAFTS -> binding = ItemSentBinding.inflate(LayoutInflater.from(mContext), parent, false)
            FolderType.DELETED -> binding = ItemEmailBinding.inflate(LayoutInflater.from(mContext), parent, false)
        }

        return MyViewHolder(binding.root, binding)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mBinding.setVariable(BR.adapter, this)
        holder.mBinding.setVariable(BR.item, mData[position])
        holder.mBinding.setVariable(BR.position, position)
        holder.mBinding.executePendingBindings()
    }

    fun setNewData(data: List<Email>) {
        mData = data
        notifyDataSetChanged()
    }

    fun goNext(item: Email, position: Int) {
        EmailDetailActivity.start2EmailDetailActivity(mContext, item.id, mType)
    }

    class MyViewHolder(itemView: View, binding: ViewDataBinding) : RecyclerView.ViewHolder(itemView) {
        var mBinding: ViewDataBinding = binding
    }
}
