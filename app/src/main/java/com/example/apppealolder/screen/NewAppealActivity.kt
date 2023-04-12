package com.example.apppealolder.screen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.db.LocaleHelper
import com.example.apppealolder.R
import com.example.apppealolder.adapter.AppealRecyclerAdapter
import com.example.apppealolder.databinding.NewAppealActivityBinding
import com.example.apppealolder.db.AsyncTaskCallback
import com.example.apppealolder.db.ReadCursorAsyncTask
import com.example.apppealolder.model.AppealInfo
import com.example.apppealolder.model.LabelWord.Companion.APPEAL_INFO
import com.example.apppealolder.model.LabelWord.Companion.ENG
import com.example.apppealolder.model.LabelWord.Companion.GO_INFO_ACTIVITY
import com.example.apppealolder.model.LabelWord.Companion.MODE_DEF
import com.example.apppealolder.model.LabelWord.Companion.MODE_LIGHT
import com.example.apppealolder.model.LabelWord.Companion.RU
import com.example.apppealolder.model.LabelWord.Companion.SHARED_KEY
import com.example.apppealolder.model.LabelWord.Companion.SHARED_MODE
import com.example.apppealolder.model.LabelWord.Companion.UZ
import com.example.apppealolder.utils.showSnackbar
import com.example.apppealolder.utils.unVisible
import com.example.apppealolder.utils.visible
import java.lang.Exception

class NewAppealActivity : AppCompatActivity() {
    private val binding: NewAppealActivityBinding by viewBinding()
    private lateinit var adapter: AppealRecyclerAdapter
    private val asyncTaskCallback = object : AsyncTaskCallback<ArrayList<AppealInfo>> {

        override fun onSuccess(listData: ArrayList<AppealInfo>?) {
            binding.progressBar.unVisible()
            adapter.differ.submitList(listData)
            binding.rvNewsId.adapter = adapter
            binding.rvNewsId.layoutManager = LinearLayoutManager(this@NewAppealActivity)
        }

        override fun onError(error: Exception?) {
            binding.progressBar.unVisible()
            binding.botNavId.showSnackbar(error?.message.toString())
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_appeal_activity)
        binding.progressBar.visible()
        adapter = AppealRecyclerAdapter()
        val readCursorAsyncTask = ReadCursorAsyncTask(this, asyncTaskCallback)
        readCursorAsyncTask.execute(0)

        adapter.onItemClick = {
            val intent = Intent(this@NewAppealActivity, AppealInfoActivity::class.java)
            intent.putExtra(APPEAL_INFO, it)
            startActivityForResult(intent, GO_INFO_ACTIVITY)

        }

        binding.botNavId.selectedItemId = R.id.newAppealsScreen
        binding.botNavId.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.profileScreen -> {
                    change(SettingsActivity())
                    true
                }
                R.id.historyAppealsScreen -> {
                    change(HistoryAppealActivity())
                    true
                }


                else -> true

            }
        }

        checkLang()
        checkMode()


    }


    private fun change(activity: AppCompatActivity) {
        startActivity(Intent(this, activity::class.java))
        finish()
    }


    private fun checkLang() {
        when (LocaleHelper.savedLang(this)) {
            ENG -> {
                LocaleHelper.changeLanguage(ENG, this)
            }
            RU -> {
                LocaleHelper.changeLanguage(RU, this)
            }
            else -> {
                LocaleHelper.changeLanguage(UZ, this)
            }
        }
    }

    private fun checkMode() {
        val modeSharedPref = getSharedPreferences(SHARED_MODE, Context.MODE_PRIVATE)
        val mode = modeSharedPref.getString(SHARED_KEY, MODE_DEF)
        if (mode == MODE_LIGHT) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GO_INFO_ACTIVITY && resultCode == RESULT_OK) {
            val data = data?.getSerializableExtra(APPEAL_INFO) as AppealInfo
            if (data.isAllow == 1) {
                data.isAllow = 0
                val list = adapter.differ.currentList.toMutableList()
                list.remove(data)
                adapter.differ.submitList(list)
            }
        }

    }


}