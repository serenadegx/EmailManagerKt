package com.fantaike.emailmanager.emails.draft

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
import com.fantaike.emailmanagerkt.databinding.FragmentSentBinding
import com.fantaike.emailmanagerkt.emails.adapter.EmailsListAdapter
import com.fantaike.emailmanagerkt.utils.EMDecoration

class DraftFragment : Fragment() {
    private lateinit var mBinding: FragmentSentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentSentBinding.inflate(inflater, container, false).apply {
            viewModel = (activity as MainActivity).obtainViewModel()
            lifecycleOwner = this@DraftFragment
        }
        mBinding.rv.layoutManager = LinearLayoutManager(activity)
        mBinding.rv.addItemDecoration(EMDecoration(activity, EMDecoration.VERTICAL_LIST, R.drawable.list_divider, 0))
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.rv.adapter = EmailsListAdapter(emptyList(), FolderType.DRAFTS)
        EmailApplication.account?.run {
            mBinding.viewModel?.start(FolderType.DRAFTS, this)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {
        fun newInstance() = DraftFragment()
    }
}