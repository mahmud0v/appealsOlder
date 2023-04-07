package com.example.apppealolder.screen

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.model.LabelWord
import com.example.apppealolder.db.LocaleHelper
import com.example.apppealolder.R
import com.example.apppealolder.adapter.AppealAdapter
import com.example.apppealolder.databinding.ActivityMainBinding
import com.example.apppealolder.db.AsyncTaskCallback
import com.example.apppealolder.db.DBHelper
import com.example.apppealolder.db.MainAsyncTask
import com.example.apppealolder.model.AppealInfo
import com.google.android.material.snackbar.Snackbar
import java.sql.SQLException

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()
    private val adapter: AppealAdapter by lazy { AppealAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecycler()
        manageBotNavView()
        checkLang()
        checkMode()


    }


    private fun initRecycler() {
        val asyncTaskCallback = object : AsyncTaskCallback {

            override fun onSuccess(listData: ArrayList<AppealInfo>?) {
                hideLoader()
                adapter.differ.submitList(listData)
                binding.newsAppealsId.rvNewsId.adapter = adapter
                binding.newsAppealsId.rvNewsId.layoutManager =
                    LinearLayoutManager(this@MainActivity)
            }

            override fun onError(error: String) {
                hideLoader()
                Snackbar.make(binding.progressBar, error, Snackbar.LENGTH_SHORT).show()

            }

            override fun onLoading() {
                showLoader()

            }

        }

        val mainAsyncTask = MainAsyncTask(this@MainActivity, asyncTaskCallback)
        mainAsyncTask.execute()
        itemClickEvent()

    }

    private fun itemClickEvent() {
        adapter.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("key", it)
            }
            val intent = Intent(this@MainActivity, InfoItemScreen::class.java)
            intent.putExtra("key", bundle)
            startActivityForResult(intent, 1)
//            finish()


        }
    }

    private fun manageBotNavView() {
        binding.botNavId.selectedItemId = R.id.newAppealsScreen
        binding.botNavId.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.profileScreen -> {
                    change(ProfileActivity())
                    true
                }
                R.id.historyAppealsScreen -> {
                    change(HistoryAppealScreen())
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



    private fun checkLang() {
        val storedLang = LocaleHelper.savedLang(this)
        if (storedLang == "en") {
            LocaleHelper.changeLanguage("en", this)
        } else if (storedLang == "ru") {
            LocaleHelper.changeLanguage("ru", this)
        } else {
            LocaleHelper.changeLanguage("uz", this)
        }
    }

    private fun checkMode() {
        val modeSharedPref = getSharedPreferences("modeShared", Context.MODE_PRIVATE)
        val mode = modeSharedPref.getString("key", "light")
        if (mode == "light") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        }
    }

    private fun showLoader() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        initRecycler()
    }


}