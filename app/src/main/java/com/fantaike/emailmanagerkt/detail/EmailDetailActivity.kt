package com.fantaike.emailmanager.detail

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.send.SendEmailActivity
import com.fantaike.emailmanagerkt.EmailApplication
import com.fantaike.emailmanagerkt.R
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.data.FolderType
import com.fantaike.emailmanagerkt.data.SendType
import com.fantaike.emailmanagerkt.databinding.ActivityEmailDetailBinding
import com.fantaike.emailmanagerkt.detail.DetailNavigator
import com.fantaike.emailmanagerkt.detail.adapter.AttachmentListAdapter
import com.fantaike.emailmanagerkt.utils.obtainViewModel
import com.google.android.material.snackbar.Snackbar

class EmailDetailActivity : AppCompatActivity(), DetailNavigator {


    private lateinit var mBinding: ActivityEmailDetailBinding
    private var mData: List<Attachment> = emptyList()
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =
            DataBindingUtil.setContentView<ActivityEmailDetailBinding>(this, R.layout.activity_email_detail)
                .apply {
                    viewModel = obtainViewModel()
                    lifecycleOwner = this@EmailDetailActivity
                    rvAttachment.layoutManager = LinearLayoutManager(this@EmailDetailActivity)
                }
        setupToolbar()
        setupAdapter()
        setupSnackBar()
        subscribeChanges2Navigator()
        initData()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (intent.getSerializableExtra("type") as FolderType == FolderType.DRAFTS)
            menuInflater.inflate(R.menu.detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return super.onOptionsItemSelected(item)
    }

    override fun showDeleteConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("提示信息")
            .setMessage("是否要删除邮件")
            .setNegativeButton("取消") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("确定") { dialog, which ->
                dialog.cancel()
                obtainViewModel().deleteEmail()
            }
            .show()
    }

    override fun reply(email: Email) {
        SendEmailActivity.start2SendEmailActivity(this, email, SendType.REPLY)
    }

    override fun forward(email: Email) {
        SendEmailActivity.start2SendEmailActivity(this, email, SendType.FORWARD)
    }

    private fun subscribeChanges2Navigator() {
        obtainViewModel().deleteEvent.observe(this, Observer {
            Thread {
                Snackbar.make(mBinding.root, "删除成功", Snackbar.LENGTH_SHORT).show()
                SystemClock.sleep(1000)
                finish()
            }.start()
        })
        obtainViewModel().loadingEvent.observe(this, Observer {
            if (it.isShow) {
                dialog = ProgressDialog.show(this, "", it.msg, false, false)
            } else {
                dialog?.cancel()
            }
        })
    }

    private fun initData() {
        EmailApplication.account?.run {
            mBinding.viewModel?.start(
                intent.getLongExtra("id", 0L),
                intent.getSerializableExtra("type") as FolderType, this,
                this@EmailDetailActivity
            )
        }
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

    private fun obtainViewModel() = obtainViewModel(EmailViewModel::class.java)

    companion object {
        fun start2EmailDetailActivity(context: Context, id: Long, type: FolderType) =
            context.startActivity(
                Intent(context, EmailDetailActivity::class.java)
                    .putExtra("id", id)
                    .putExtra("type", type)
            )
    }
}


