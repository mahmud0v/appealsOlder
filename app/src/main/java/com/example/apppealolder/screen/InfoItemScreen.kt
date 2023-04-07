package com.example.apppealolder.screen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.model.LabelWord
import com.example.apppealolder.R
import com.example.apppealolder.databinding.InfoItemScreenBinding
import com.example.apppealolder.db.AppealsAsyncTask
import com.example.apppealolder.db.AsyncTaskCallback
import com.example.apppealolder.model.AppealInfo
import com.google.android.material.snackbar.Snackbar

class InfoItemScreen : AppCompatActivity() {
    private val binding: InfoItemScreenBinding by viewBinding()
    private lateinit var data: AppealInfo
    private var firstValue: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_item_screen)
        initToolbar()
        loadData()
        clickEventAllowed()


    }


    private fun loadData() {
        data = intent.getBundleExtra("key")?.getSerializable("key") as AppealInfo
        binding.numberText.text = data.phone_number
        binding.districtNameId.text = data.district
        binding.requestDate.text = data.request_data
        binding.descId.text = data.description
        binding.allowedTextId.text = isAllowText(data.isAllow)
        firstValue = data.isAllow
    }

    private fun isAllowText(value: Int): String {
        if (value == 0) {
            return LabelWord.pendingAllowed
        } else {
            binding.allowBtn.visibility = View.GONE
            return LabelWord.allowed
        }
    }

    private fun clickEventAllowed() {
        val asyncTaskCallback = object : AsyncTaskCallback {
            override fun onSuccess(listData: ArrayList<AppealInfo>?) {

            }

            override fun onError(error: String) {
                Snackbar.make(binding.allowBtn, error, Snackbar.LENGTH_SHORT).show()
            }

            override fun onLoading() {

            }

        }
        binding.allowBtn.setOnClickListener {
            val myAsyncTask = AppealsAsyncTask(this,asyncTaskCallback)
            myAsyncTask.execute(data.id)
            binding.allowedTextId.text = isAllowText(1)
            binding.allowBtn.visibility = View.GONE
        }

    }


    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            back()
        }
    }

    private fun back() {
        val intent = Intent()
        intent.putExtra("key", data.id)
        setResult(5, intent)
        finish()

    }


}


