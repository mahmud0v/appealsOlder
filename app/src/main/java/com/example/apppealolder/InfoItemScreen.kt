package com.example.apppealolder

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.databinding.InfoItemScreenBinding

class InfoItemScreen : AppCompatActivity() {
    private val binding: InfoItemScreenBinding by viewBinding()
    private lateinit var dbHelper: DBHelper
    private var data: AppealInfo? = null
    private var firstValue: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_item_screen)
        loadData()
        clickEventAllowed()
        clickBack()
        changeBackIcon()


    }


    private fun loadData() {
        dbHelper = DBHelper.getInstance(this)
        data = intent.getBundleExtra("key")?.getSerializable("key") as AppealInfo
        binding.numberText.text = data!!.phone_number
        binding.districtNameId.text = data!!.district
        binding.requestDate.text = data!!.request_data
        binding.descId.text = data!!.description
        binding.allowedTextId.text = isAllowText(data!!.isAllow)
        firstValue = data!!.isAllow
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
        val db = dbHelper.writableDatabase
        binding.allowBtn.setOnClickListener {
            data!!.isAllow = 1
            val contentValue = ContentValues()
            contentValue.apply {
                put("id", data!!.id)
                put("phone_number", data!!.phone_number)
                put("district", data!!.district)
                put("request_data", data!!.request_data)
                put("description", data!!.description)
                put("isAllow", data!!.isAllow)

            }
            db.update("Appeals", contentValue, "id=?", arrayOf(data!!.id.toString()))
            binding.allowedTextId.text = isAllowText(data!!.isAllow)
            binding.allowBtn.visibility = View.GONE

        }

    }

    private fun clickBack() {
        binding.backId.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (firstValue == 1) {
            change(HistoryAppealScreen())
        } else {
            change(MainActivity())
        }
    }

    private fun change(activity: AppCompatActivity) {
        startActivity(Intent(this, activity::class.java))
        finish()
    }

    private fun changeBackIcon() {
        val modeSharedPref = getSharedPreferences("modeShared", Context.MODE_PRIVATE)
        val mode = modeSharedPref.getString("key", "light")
        if (mode == "light") {
            binding.backId.setImageResource(R.drawable.back_icon)
        } else {
            binding.backId.setImageResource(R.drawable.night_back_icon)
        }
    }


}


