package com.fantaike.emailmanager.detail

import android.Manifest
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fantaike.emailmanager.data.Email
import com.fantaike.emailmanager.data.source.EmailDataSource
import com.fantaike.emailmanager.send.SendEmailActivity
import com.fantaike.emailmanagerkt.EmailApplication
import com.fantaike.emailmanagerkt.R
import com.fantaike.emailmanagerkt.data.Attachment
import com.fantaike.emailmanagerkt.data.FolderType
import com.fantaike.emailmanagerkt.data.SendType
import com.fantaike.emailmanagerkt.databinding.ActivityEmailDetailBinding
import com.fantaike.emailmanagerkt.detail.DetailNavigator
import com.fantaike.emailmanagerkt.detail.adapter.AttachmentListAdapter
import com.fantaike.emailmanagerkt.utils.checkPermission
import com.fantaike.emailmanagerkt.utils.obtainViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.File

class EmailDetailActivity : AppCompatActivity(), DetailNavigator {
    private lateinit var item: Attachment
    private var index: Int = 0
    private lateinit var mBinding: ActivityEmailDetailBinding
    private var mData: List<Attachment> = emptyList()
    private var dialog: ProgressDialog? = null
    private lateinit var manager: NotificationManager
    private lateinit var notification: Notification
    private lateinit var builder: NotificationCompat.Builder
    private lateinit var adapter: AttachmentListAdapter

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
        SendEmailActivity.start2SendEmailActivity(this, intent.getParcelableExtra("email"), SendType.EDIT)
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

    override fun onStart(index: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelID: String = "channelID$index"
            val channelName: String = "channelID$index"
            manager.createNotificationChannel(
                NotificationChannel(
                    channelID,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableLights(true)
                    enableVibration(true)
                    importance = NotificationManager.IMPORTANCE_HIGH
                }
            )
        }
        builder = NotificationCompat.Builder(this, "$index")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentTitle("附件:${obtainViewModel().items.value?.get(index)?.fileName}")
            .setAutoCancel(false)
            .setContentText("进度:0%")
            .setProgress(100, 0, false)
        notification = builder.build()
        manager.notify(index, notification)
    }

    override fun onProgress(index: Int, percent: Float) {
        builder.setProgress(100, (percent * 100).toInt(), false)
        builder.setContentText("进度:${(percent * 100).toInt()}%")
        notification = builder.build()
        manager.notify(index, notification)
    }

    override fun onFinish(index: Int) {
        obtainViewModel().items.value?.get(index)?.isDownload = true
        builder.setContentText("下载完成")
            .setProgress(100, 100, false)
            .setAutoCancel(true)
        notification = builder.build()
        manager.notify(index, notification)
        adapter.notifyDataSetChanged()
    }

    override fun onError(index: Int) {
        builder.setContentTitle("附件:${obtainViewModel().items.value?.get(index)?.fileName}")
            .setContentText("下载失败")
            .setAutoCancel(true)
        notification = builder.build()
        manager.notify(index, notification)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 715 && checkPermission(this, permissions)) {
            obtainViewModel().download(
                intent.getSerializableExtra("type") as FolderType,
                intent.getLongExtra("id", 0L), index,
                File(Environment.getExternalStorageDirectory(), "EmailManager").absolutePath,
                item.fileName
            )
        }
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
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        EmailApplication.account?.run {
            mBinding.viewModel?.start(
                intent.getLongExtra("id", 0L),
                intent.getSerializableExtra("type") as FolderType, this,
                this@EmailDetailActivity
            )
        }
    }

    private fun setupSnackBar() {
        obtainViewModel().snackBarMessage.observe(this, Observer<String> {
            it?.let { Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show() }
        })
    }

    private fun setupAdapter() {
        adapter = AttachmentListAdapter(mData)
        adapter.setDownloadListener(object : AttachmentListAdapter.DownloadListener {
            override fun onItemClickListener(item: Attachment, index: Int) {
                this@EmailDetailActivity.index = index
                this@EmailDetailActivity.item = item
                //存储权限适配
                if (ContextCompat.checkSelfPermission(
                        this@EmailDetailActivity, Manifest.permission
                            .WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@EmailDetailActivity, arrayOf(
                            Manifest.permission
                                .WRITE_EXTERNAL_STORAGE
                        ), 715
                    )
                } else {
                    obtainViewModel().download(
                        intent.getSerializableExtra("type") as FolderType,
                        intent.getLongExtra("id", 0L), index,
                        File(Environment.getExternalStorageDirectory(), "EmailManager").absolutePath,
                        item.fileName
                    )
                }

            }

        })
        mBinding.rvAttachment.adapter = adapter
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


