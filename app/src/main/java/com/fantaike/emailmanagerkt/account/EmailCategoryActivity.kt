package com.fantaike.emailmanager.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.fantaike.emailmanager.data.Configuration
import com.fantaike.emailmanagerkt.EmailApplication
import com.fantaike.emailmanagerkt.R
import com.fantaike.emailmanagerkt.account.adapter.EmailCategoryAdapter
import com.fantaike.emailmanagerkt.databinding.ActivityEmailCategoryBinding

class EmailCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailCategoryBinding
    private var configs = mutableListOf<Configuration>()
    private lateinit var mAdapter: EmailCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_category)
        binding.rv.layoutManager = LinearLayoutManager(this)

        initToolbar()
        initAdapter()
        initData()
    }

    private fun initAdapter() {
        mAdapter = EmailCategoryAdapter(configs)
        binding.rv.adapter = mAdapter
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initData() {
        val qqEmail = resources.getStringArray(R.array.qq_email)
        configs.add(
            Configuration(
                1,
                qqEmail[0],
                qqEmail[1],
                qqEmail[2],
                qqEmail[3],
                qqEmail[4],
                qqEmail[5],
                qqEmail[6],
                "1".equals(qqEmail[7]),
                qqEmail[8],
                qqEmail[9],
                qqEmail[10],
                qqEmail[11],
                qqEmail[12],
                qqEmail[13],
                "1".equals(qqEmail[14]),
                qqEmail[15],
                "1".equals(qqEmail[16])
            )
        )
        val qqExmail = resources.getStringArray(R.array.qq_exmail)
        configs.add(
            Configuration(
                2,
                qqExmail[0],
                qqExmail[1],
                qqExmail[2],
                qqExmail[3],
                qqExmail[4],
                qqExmail[5],
                qqExmail[6],
                "1".equals(qqExmail[7]),
                qqExmail[8],
                qqExmail[9],
                qqExmail[10],
                qqExmail[11],
                qqExmail[12],
                qqExmail[13],
                "1".equals(qqExmail[14]),
                qqExmail[15],
                "1".equals(qqExmail[16])
            )
        )
        mAdapter.setNewData(configs)
        EmailApplication.configs = configs
    }


    companion object {
        fun start2EmailCategoryActivity(context: Context) {
            context.startActivity(Intent(context, EmailCategoryActivity::class.java))
        }
    }
}