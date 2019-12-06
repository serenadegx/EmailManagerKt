package com.fantaike.emailmanager.emails.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanagerkt.EmailApplication
import com.fantaike.emailmanagerkt.MainActivity
import com.fantaike.emailmanagerkt.R
import com.fantaike.emailmanagerkt.databinding.FragmentInboxBinding
import com.fantaike.emailmanagerkt.emails.adapter.EmailsListAdapter
import com.fantaike.emailmanagerkt.utils.EMDecoration

class InboxFragment : Fragment() {
    private lateinit var mBinding: FragmentInboxBinding
    private var mData: List<Email> = emptyList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentInboxBinding.inflate(inflater, container, false).apply {
            //初始化
            viewModel = (activity as MainActivity).obtainViewModel()
        }
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.rv.layoutManager = LinearLayoutManager(activity)
        mBinding.rv.addItemDecoration(EMDecoration(activity, EMDecoration.VERTICAL_LIST, R.drawable.list_divider, 0))
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.rv.adapter = EmailsListAdapter(mData)
        EmailApplication.account?.run {
            mBinding.viewModel?.start(0, this)
        }
    }

    override fun onStart() {
        super.onStart()

    }

    companion object {
        fun newInstance() = InboxFragment()
    }
}