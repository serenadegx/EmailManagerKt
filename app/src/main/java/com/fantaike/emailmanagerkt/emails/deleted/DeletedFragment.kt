package com.fantaike.emailmanagerkt.emails.deleted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fantaike.emailmanagerkt.EmailApplication
import com.fantaike.emailmanagerkt.MainActivity
import com.fantaike.emailmanagerkt.R
import com.fantaike.emailmanagerkt.data.FolderType
import com.fantaike.emailmanagerkt.databinding.FragmentInboxBinding
import com.fantaike.emailmanagerkt.emails.adapter.EmailsListAdapter
import com.fantaike.emailmanagerkt.utils.EMDecoration

class DeletedFragment : Fragment() {
    private lateinit var mBinding: FragmentInboxBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentInboxBinding.inflate(inflater, container, false).apply {
            viewModel = (activity as MainActivity).obtainViewModel()
            lifecycleOwner = this@DeletedFragment
        }
        mBinding.rv.layoutManager = LinearLayoutManager(activity)
        mBinding.rv.addItemDecoration(EMDecoration(activity, EMDecoration.VERTICAL_LIST, R.drawable.list_divider, 0))
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.rv.adapter = EmailsListAdapter(emptyList(), FolderType.DELETED)
        EmailApplication.account?.run {
            mBinding.viewModel?.start(FolderType.DELETED, this)
        }
    }

    companion object {
        fun newInstance() = DeletedFragment()
    }
}