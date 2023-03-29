package com.example.apppealolder

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.icu.text.AlphabeticIndex.Bucket.LabelType
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.databinding.ActivityMainBinding
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecycler()
        manageBotNavView()
        checkLang()
        checkMode()


    }

    private fun loadData(): ArrayList<AppealInfo> {

        dbHelper = DBHelper.getInstance(this)
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "Appeals",
            arrayOf("id", "phone_number", "district", "request_data", "description", "isAllow"),
            "isAllow= ?",
            arrayOf("0"),
            null,
            null,
            null
        )

        val indexId = cursor.getColumnIndex("id")
        val indexPhone = cursor.getColumnIndex("phone_number")
        val indexDistrict = cursor.getColumnIndex("district")
        val indexRequest = cursor.getColumnIndex("request_data")
        val indexDescription = cursor.getColumnIndex("description")
        val indexAllow = cursor.getColumnIndex("isAllow")

        val arrayList = ArrayList<AppealInfo>()
        while (cursor.moveToNext()) {
            val data = AppealInfo(
                cursor.getInt(indexId),
                cursor.getString(indexPhone),
                cursor.getString(indexDistrict),
                cursor.getString(indexRequest),
                cursor.getString(indexDescription),
                cursor.getInt(indexAllow)

            )

            arrayList.add(data)
        }
        cursor.close()
        return arrayList


    }


    private fun initRecycler() {
        val adapter = AppealAdapter(LabelWord.pendingAllowed)
        adapter.differ.submitList(loadData())
        binding.newsAppealsId.rvNewsId.adapter = adapter
        binding.newsAppealsId.rvNewsId.layoutManager = LinearLayoutManager(this)

        adapter.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("key", it)
            }
            val intent = Intent(this, InfoItemScreen::class.java)
            intent.putExtra("key", bundle)
            startActivity(intent)
            finish()


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

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
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


}