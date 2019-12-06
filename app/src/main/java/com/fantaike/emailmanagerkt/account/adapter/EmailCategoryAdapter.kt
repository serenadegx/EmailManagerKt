package com.fantaike.emailmanagerkt.account.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fantaike.emailmanager.account.EmailCategoryActivity
import com.fantaike.emailmanager.account.VerifyActivity
import com.fantaike.emailmanager.data.Configuration
import com.fantaike.emailmanagerkt.BR
import com.fantaike.emailmanagerkt.databinding.ItemEmailCatogoryBinding

class EmailCategoryAdapter(data: List<Configuration>) : RecyclerView.Adapter<EmailCategoryAdapter.MyViewHolder>() {
    private var mData: List<Configuration> = data
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context;
        val mBinding = ItemEmailCatogoryBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return MyViewHolder(mBinding.root,mBinding)
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

    fun setNewData(data: List<Configuration>) {
        mData = data
        notifyDataSetChanged()
    }

    fun goNext(item: Configuration, position: Int) {
        VerifyActivity.start2VerifyActivity(mContext, item.categoryId)
    }

    class MyViewHolder(itemView: View, binding: ItemEmailCatogoryBinding) : RecyclerView.ViewHolder(itemView) {
        var mBinding: ItemEmailCatogoryBinding = binding
    }
}
