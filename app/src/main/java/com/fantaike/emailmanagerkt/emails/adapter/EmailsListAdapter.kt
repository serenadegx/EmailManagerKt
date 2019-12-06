package com.fantaike.emailmanagerkt.emails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.detail.EmailDetailActivity
import com.fantaike.emailmanagerkt.BR
import com.fantaike.emailmanagerkt.databinding.ItemEmailBinding

class EmailsListAdapter(data: List<Email>) : RecyclerView.Adapter<EmailsListAdapter.MyViewHolder>() {
    private var mData: List<Email> = data
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context;
        val binding = ItemEmailBinding.inflate(LayoutInflater.from(mContext), parent, false)
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
        EmailDetailActivity.start2EmailDetailActivity(mContext, item.id)
    }

    class MyViewHolder(itemView: View, binding: ItemEmailBinding) : RecyclerView.ViewHolder(itemView) {
        var mBinding: ItemEmailBinding = binding
    }
}
