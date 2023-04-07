package com.example.apppealolder.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.R
import com.example.apppealolder.adapter.AppealAdapter
import com.example.apppealolder.databinding.HistoryAppealScreenBinding
import com.example.apppealolder.db.AsyncTaskCallback
import com.example.apppealolder.db.HistoryAsyncTask
import com.example.apppealolder.model.AppealInfo
import com.google.android.material.snackbar.Snackbar

class HistoryAppealScreen : AppCompatActivity() {
    private val binding: HistoryAppealScreenBinding by viewBinding()
    private val adapter: AppealAdapter by lazy { AppealAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_appeal_screen)
        manageBotNavView()
        readData()
        itemClickEvent()

    }

    private fun readData() {
        val asyncTaskCallback = object : AsyncTaskCallback {


            override fun onSuccess(listData: ArrayList<AppealInfo>) {
                hideLoader()
                adapter.differ.submitList(listData)
                binding.historyId.rvNewsId.adapter = adapter
                binding.historyId.rvNewsId.layoutManager =
                    LinearLayoutManager(this@HistoryAppealScreen)

            }

            override fun onError(error: String) {
                hideLoader()
                Snackbar.make(binding.progressBar, error, Snackbar.LENGTH_SHORT).show()


            }

            override fun onLoading() {
                showLoader()

            }

        }

        val historyAsyncTask = HistoryAsyncTask(this, asyncTaskCallback)
        historyAsyncTask.execute()


    }


    private fun itemClickEvent() {
        adapter.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("key", it)
            }
            val intent = Intent(this, InfoItemScreen::class.java)
            intent.putExtra("key", bundle)
            startActivity(intent)
//            finish()
        }

    }

    private fun manageBotNavView() {
        binding.botNavId.selectedItemId = R.id.historyAppealsScreen
        binding.botNavId.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.newAppealsScreen -> {
                    change(MainActivity())
                    true
                }
                R.id.profileScreen -> {
                    change(ProfileActivity())
                    true
                }


                else -> true

            }
        }
    }

    private fun change(activity: AppCompatActivity) {
        startActivity(Intent(this, activity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }




    private fun showLoader() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.progressBar.visibility = View.GONE
    }


}