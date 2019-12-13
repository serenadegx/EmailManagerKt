package com.fantaike.emailmanager.send

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.multifile.XRMultiFile
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanagerkt.EmailApplication
import com.fantaike.emailmanagerkt.R
import com.fantaike.emailmanagerkt.data.SendType
import com.fantaike.emailmanagerkt.databinding.ActivitySendEmailBinding
import com.fantaike.emailmanagerkt.send.adapter.AttachmentListAdapter
import com.fantaike.emailmanagerkt.utils.obtainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.concurrent.thread

class SendEmailActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySendEmailBinding
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivitySendEmailBinding>(this, R.layout.activity_send_email).apply {
            rvAttachment.layoutManager = LinearLayoutManager(this@SendEmailActivity)
            viewModel = obtainViewModel()
            lifecycleOwner = this@SendEmailActivity
        }
        setupToolbar()
        setupAdapter()
        subscribeChanges2Navigator()
    }

    override fun onStart() {
        super.onStart()
        EmailApplication.account?.run {
            obtainViewModel(SendEmailViewModel::class.java).start(
                this, intent.getParcelableExtra("email"),
                intent.getSerializableExtra("type") as SendType
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.send, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_send -> obtainViewModel().send(true)
            R.id.action_attach -> openFile()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        obtainViewModel(SendEmailViewModel::class.java).handleOnActivityResult(requestCode, resultCode, data)
    }

    private fun openFile() {
        XRMultiFile.get()
            .with(this)
            .lookHiddenFile(false)
            .limit(3)
            .select(this, 715)
    }

    private fun subscribeChanges2Navigator() {
        obtainViewModel().sentEvent.observe(this, Observer {
            Thread {
                Snackbar.make(mBinding.root, "发送成功", Snackbar.LENGTH_SHORT).show()
                SystemClock.sleep(1000)
                finish()
            }.start()

        })
        obtainViewModel().saveEvent.observe(this, Observer {
            Thread {
                Snackbar.make(mBinding.root, "保存成功", Snackbar.LENGTH_SHORT).show()
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
        obtainViewModel().snackBarText.observe(this, Observer {
            Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun setupAdapter() {
        mBinding.rvAttachment.adapter = AttachmentListAdapter(emptyList())
    }

    private fun setupToolbar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun obtainViewModel() = obtainViewModel(SendEmailViewModel::class.java)

    companion object {

        fun start2SendEmailActivity(context: Context, type: SendType) =
            context.startActivity(
                Intent(context, SendEmailActivity::class.java)
                    .putExtra("type", type)
            )

        fun start2SendEmailActivity(context: Context, email: Email?, type: SendType) =
            context.startActivity(
                Intent(context, SendEmailActivity::class.java)
                    .putExtra("email", email)
                    .putExtra("type", type)
            )
    }
}