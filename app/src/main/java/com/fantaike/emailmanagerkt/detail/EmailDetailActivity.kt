package com.fantaike.emailmanager.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fantaike.emailmanagerkt.EmailApplication
import com.fantaike.emailmanagerkt.R
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.databinding.ActivityEmailDetailBinding
import com.fantaike.emailmanagerkt.detail.adapter.AttachmentListAdapter
import com.fantaike.emailmanagerkt.utils.obtainViewModel
import com.google.android.material.snackbar.Snackbar

class EmailDetailActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityEmailDetailBinding
    private var mData: List<Attachment> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =
            DataBindingUtil.setContentView<ActivityEmailDetailBinding>(this, R.layout.activity_email_detail)
                .apply {
                    viewModel = obtainViewModel(EmailViewModel::class.java)
                    lifecycleOwner = this@EmailDetailActivity
                    rvAttachment.layoutManager = LinearLayoutManager(this@EmailDetailActivity)
                }
        setupToolbar()
        setupAdapter()
        setupSnackBar()
        initData()
    }

    private fun initData() {
        EmailApplication.account?.run { mBinding.viewModel?.start(intent.getLongExtra("id", 0L), this) }
    }

    private fun setupSnackBar() {
        obtainViewModel(EmailViewModel::class.java).snackBarMessage.observe(this, Observer<String> {
            it?.let { Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show() }
        })
    }

    private fun setupAdapter() {
        mBinding.rvAttachment.adapter = AttachmentListAdapter(mData)
    }

    private fun setupToolbar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        mBinding.toolbar.setNavigationOnClickListener { finish() }
    }

    companion object {
        fun start2EmailDetailActivity(context: Context, id: Long) =
            context.startActivity(Intent(context, EmailDetailActivity::class.java).putExtra("id", id))
    }
}