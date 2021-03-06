package com.fantaike.emailmanagerkt.send.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fantaike.emailmanagerkt.BR
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.databinding.ItemAddAttachmentBinding
import com.fantaike.emailmanagerkt.databinding.ItemAttachmentBinding

class   AttachmentListAdapter(private var mData:List<Attachment>) :RecyclerView.Adapter<AttachmentListAdapter.MyViewHolder>(){
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context;
        val mBinding = ItemAddAttachmentBinding.inflate(LayoutInflater.from(mContext), parent, false)
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

    fun setNewData(data: List<Attachment>) {
        mData = data
        notifyDataSetChanged()
    }

    fun delete(item: Attachment, position: Int) {

    }

    class MyViewHolder(itemView: View,binding:ItemAddAttachmentBinding) : RecyclerView.ViewHolder(itemView) {
        val mBinding = binding
    }
}