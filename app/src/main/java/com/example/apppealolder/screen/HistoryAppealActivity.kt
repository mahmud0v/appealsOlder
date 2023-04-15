package com.example.apppealolder.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.R
import com.example.apppealolder.adapter.AppealRecyclerAdapter
import com.example.apppealolder.databinding.HistoryAppealActivityBinding
import com.example.apppealolder.db.AsyncTaskCallback
import com.example.apppealolder.db.ReadHistoryAppealAsyncTask
import com.example.apppealolder.model.AppealInfo
import com.example.apppealolder.model.LabelWord.Companion.APPEAL_INFO
import com.example.apppealolder.utils.*
import java.lang.Exception

class HistoryAppealActivity : AppCompatActivity() {
    private val binding: HistoryAppealActivityBinding by viewBinding()
    private lateinit var historyAppealsAdapter: AppealRecyclerAdapter
    private val asyncTaskCallback = object : AsyncTaskCallback<ArrayList<AppealInfo>> {
        override fun onSuccess(data: ArrayList<AppealInfo>?) {
            binding.progressBar.unVisible()
            historyAppealsAdapter.differ.submitList(data)

        }

        override fun onError(error: Exception?) {
            binding.progressBar.unVisible()
            binding.toolbarId.showSnackbar(error?.message.toString())
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_appeal_activity)
        historyAppealsAdapter = AppealRecyclerAdapter()
        binding.progressBar.visible()
        binding.botNavId.selectedItemId = R.id.historyAppealsScreen
        binding.rvNewsId.adapter = historyAppealsAdapter
        binding.rvNewsId.layoutManager = LinearLayoutManager(this@HistoryAppealActivity)
        binding.botNavId.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.newAppealsScreen -> {
                    change(NewAppealsActivity())
                    true
                }
                R.id.profileScreen -> {
                    change(SettingsActivity())
                    true
                }


                else -> true

            }
        }


        val readCursorAsyncTask = ReadHistoryAppealAsyncTask(this, asyncTaskCallback)
        readCursorAsyncTask.execute()
        historyAppealsAdapter.onItemClick = {
            val intent = Intent(this, AppealInfoActivity::class.java)
            intent.putExtra(APPEAL_INFO, it)
            startActivity(intent)
        }


    }


    private fun change(activity: AppCompatActivity) {
        startActivity(Intent(this, activity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, NewAppealsActivity::class.java))
    }


}