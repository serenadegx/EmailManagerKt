package com.fantaike.emailmanager.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.fantaike.emailmanagerkt.MainActivity
import com.fantaike.emailmanagerkt.R
import com.fantaike.emailmanagerkt.databinding.ActivityVerifyBinding
import com.fantaike.emailmanagerkt.utils.obtainViewModel
import com.google.android.material.snackbar.Snackbar

class VerifyActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityVerifyBinding
    private lateinit var mViewModel: VerifyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = (DataBindingUtil.setContentView(this, R.layout.activity_verify) as ActivityVerifyBinding).apply {
            viewModel = obtainViewModel(VerifyViewModel::class.java)
            lifecycleOwner = this@VerifyActivity
        }
        obtainViewModel(VerifyViewModel::class.java).verify.observe(this, Observer<Boolean> {
            if (it) {
                MainActivity.start2MainActivity(this)
            }
        })
        obtainViewModel(VerifyViewModel::class.java).snackBarMessage.observe(this, Observer<String> {
            it?.let {
                Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        obtainViewModel(VerifyViewModel::class.java).start(intent.getLongExtra("id", 0))
    }

    companion object {
        fun start2VerifyActivity(context: Context, categoryId: Long) =
            context.startActivity(Intent(context, VerifyActivity::class.java).putExtra("id", categoryId))
    }
}