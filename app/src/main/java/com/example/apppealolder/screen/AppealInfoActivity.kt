package com.example.apppealolder.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.model.LabelWord
import com.example.apppealolder.R
import com.example.apppealolder.databinding.AppealInfoActivityBinding
import com.example.apppealolder.db.AppealsUpdateAsyncTask
import com.example.apppealolder.db.AsyncTaskCallback
import com.example.apppealolder.model.AppealInfo
import com.example.apppealolder.model.LabelWord.Companion.APPEAL_INFO
import com.example.apppealolder.utils.showSnackbar
import com.example.apppealolder.utils.unVisible
import java.lang.Exception

class AppealInfoActivity : AppCompatActivity() {
    private val binding: AppealInfoActivityBinding by viewBinding()
    private lateinit var data: AppealInfo


    private val asyncTaskCallback = object : AsyncTaskCallback<Nothing> {
        override fun onSuccess(listData: Nothing?) {

        }

        override fun onError(error: Exception?) {
            binding.toolbar.showSnackbar(error?.message.toString())
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.appeal_info_activity)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            back()
        }

        binding.allowBtn.setOnClickListener {
            val myAsyncTask = AppealsUpdateAsyncTask(this, asyncTaskCallback)
            myAsyncTask.execute(data.id)
            data.isAllow = 1
            binding.allowedTextId.text = isAllowText(1)
            binding.allowBtn.unVisible()
        }

        loadData()


    }


    private fun loadData() {
        data = intent.getSerializableExtra(APPEAL_INFO) as AppealInfo
        binding.numberText.text = data.phone_number
        binding.districtNameId.text = data.district
        binding.requestDate.text = data.request_data
        binding.descId.text = data.description
        binding.allowedTextId.text = isAllowText(data.isAllow)
    }

    private fun isAllowText(value: Int): String {
        if (value == 0) {
            return LabelWord.pendingAllowed
        } else {
            binding.allowBtn.unVisible()
            return LabelWord.allowed
        }
    }


    private fun back() {
        val intent = Intent()
        intent.putExtra(APPEAL_INFO, data)
        setResult(RESULT_OK, intent)
        finish()

    }

    override fun onBackPressed() {
        back()
        super.onBackPressed()
    }


}


