package com.example.apppealolder.screen

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.R
import com.example.apppealolder.databinding.AddAppealActivityBinding
import com.example.apppealolder.db.AsyncTaskCallback
import com.example.apppealolder.db.InsertAppealAsyncTask
import com.example.apppealolder.model.AppealInfo
import com.example.apppealolder.model.LabelWord.Companion.ADD_APPEAL
import com.example.apppealolder.model.LabelWord.Companion.SUCCESS_MESSAGE
import com.example.apppealolder.utils.clickable
import com.example.apppealolder.utils.disable
import com.example.apppealolder.utils.showSnackbar
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date

class AddAppealActivity : AppCompatActivity() {
    private val binding: AddAppealActivityBinding by viewBinding()
    private val asyncTaskCallback = object : AsyncTaskCallback<AppealInfo> {
        override fun onSuccess(data: AppealInfo?) {
            binding.addBtn.showSnackbar(SUCCESS_MESSAGE)
            val intent = Intent()
            intent.putExtra(ADD_APPEAL, data)
            setResult(RESULT_OK, intent)
            finish()
        }

        override fun onError(error: Exception?) {
            binding.addBtn.showSnackbar(error?.message.toString())
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_appeal_activity)


        val data = SimpleDateFormat("dd/M/yyyy")
        val currentData = data.format(Date())

        binding.addBtn.showSnackbar(currentData)

        binding.descEditText.addTextChangedListener {
            manageButtonClickable()
        }

        binding.phoneEditText.addTextChangedListener {
            manageButtonClickable()
        }

        binding.districtEditText.addTextChangedListener {
            manageButtonClickable()
        }


    }


    private fun getAllInputText(): Boolean {
        val phoneNumber = "+" + binding.phoneEditText.unMaskedText.toString()
        val district = binding.districtEditText.text.toString()
        val description = binding.descEditText.text.toString()
        val isAllow = 0
        return (phoneNumber.length == 13 && phoneNumber.substring(0, 4) == "+998"
                && district.isNotBlank() && description.isNotBlank())

    }

    private fun manageButtonClickable() {
        val data = SimpleDateFormat("dd/M/yyyy")
        val requestData = data.format(Date())
        if (getAllInputText()) {
            binding.addBtn.clickable()
            binding.addBtn.setBackgroundResource(R.drawable.add_back)
            binding.addBtn.setOnClickListener {
                val contentValue = ContentValues()
                contentValue.apply {
                    put("phone_number", ("+" + binding.phoneEditText.unMaskedText.toString()))
                    put("district", binding.districtEditText.text.toString())
                    put("request_data", requestData)
                    put("description", binding.descEditText.text.toString())
                    put("isAllow", 0)
                }
                val insertAppealAsyncTask = InsertAppealAsyncTask(baseContext, asyncTaskCallback)
                insertAppealAsyncTask.execute(contentValue)

            }
        } else {
            binding.addBtn.disable()
            binding.addBtn.setBackgroundResource(R.drawable.add_disable)

        }

    }


}

