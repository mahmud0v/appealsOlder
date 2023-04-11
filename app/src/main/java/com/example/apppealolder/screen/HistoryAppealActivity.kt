package com.example.apppealolder.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.R
import com.example.apppealolder.adapter.AppealRecyclerAdapter
import com.example.apppealolder.databinding.HistoryAppealActivityBinding
import com.example.apppealolder.db.AsyncTaskCallback
import com.example.apppealolder.db.ReadCursorAsyncTask
import com.example.apppealolder.model.AppealInfo
import com.example.apppealolder.utils.*
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class HistoryAppealActivity : AppCompatActivity() {
    private val binding: HistoryAppealActivityBinding by viewBinding()
    private lateinit var adapter: AppealRecyclerAdapter
    private val asyncTaskCallback = object : AsyncTaskCallback<ArrayList<AppealInfo>> {
        override fun onSuccess(listData: ArrayList<AppealInfo>?) {
            binding.progressBar.visible()
            adapter.differ.submitList(listData)
            binding.rvNewsId.adapter = adapter
            binding.rvNewsId.layoutManager = LinearLayoutManager(this@HistoryAppealActivity)
        }

        override fun onError(error: Exception?) {
            binding.progressBar.unVisible()
            binding.toolbarId.showSnackbar(error?.message.toString())
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_appeal_activity)
        binding.progressBar.visible()
        binding.botNavId.selectedItemId = R.id.historyAppealsScreen
        binding.botNavId.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.newAppealsScreen -> {
                    change(NewAppealActivity())
                    true
                }
                R.id.profileScreen -> {
                    change(SettingsActivity())
                    true
                }


                else -> true

            }
        }


        val readCursorAsyncTask = ReadCursorAsyncTask(this, asyncTaskCallback)
        readCursorAsyncTask.execute()
        adapter.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("key", it)
            }
            val intent = Intent(this, AppealInfoActivity::class.java)
            intent.putExtra("key", bundle)
            startActivity(intent)
        }


    }


    private fun change(activity: AppCompatActivity) {
        startActivity(Intent(this, activity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, NewAppealActivity::class.java))
    }


}