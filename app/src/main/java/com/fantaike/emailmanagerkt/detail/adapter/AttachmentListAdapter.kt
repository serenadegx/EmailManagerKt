package com.fantaike.emailmanagerkt.detail.adapter

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.multifile.XRMultiFile
import com.fantaike.emailmanager.detail.EmailViewModel
import com.fantaike.emailmanagerkt.BR
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.databinding.ItemAttachmentBinding
import java.io.File

class AttachmentListAdapter(data: List<Attachment>) : RecyclerView.Adapter<AttachmentListAdapter.MyViewHolder>() {
    private var mData: List<Attachment> = data
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context;
        val mBinding = ItemAttachmentBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return MyViewHolder(mBinding.root, mBinding)
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

    fun downloadOrOpen(item: Attachment, position: Int) {
        if (item.isDownload) {
            XRMultiFile.get().with(mContext).custom(File(Environment.getExternalStorageDirectory(), "/EmailManager"))
                .browse()
        } else {
            listener?.onItemClickListener(item, position)
        }
    }

    class MyViewHolder(itemView: View, binding: ItemAttachmentBinding) : RecyclerView.ViewHolder(itemView) {
        val mBinding = binding
    }

    interface DownloadListener {
        fun onItemClickListener(item: Attachment, index: Int)
    }

    var listener: DownloadListener? = null

    fun setDownloadListener(listener: DownloadListener) {
        this.listener = listener
    }
}