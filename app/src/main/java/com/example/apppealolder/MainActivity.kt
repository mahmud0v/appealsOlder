package com.example.apppealolder

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.icu.text.AlphabeticIndex.Bucket.LabelType
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.databinding.ActivityMainBinding
import java.io.FileOutputStream
import java.sql.SQLException

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()
    private var adapter: AppealAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecycler()
        manageBotNavView()
        checkLang()
        checkMode()


    }


    private fun initRecycler() {
        adapter = AppealAdapter(LabelWord.pendingAllowed)
        val myAsyncTask = MyAsyncTask()
        myAsyncTask.execute()

    }

    private fun itemClickEvent() {
        adapter!!.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("key", it)
            }
            val intent = Intent(this@MainActivity, InfoItemScreen::class.java)
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


    inner class MyAsyncTask() : AsyncTask<Unit, Unit, Boolean>() {

        private var list: ArrayList<AppealInfo>? = null

        override fun onPreExecute() {
            list = ArrayList()
        }

        override fun doInBackground(vararg p0: Unit?): Boolean? {
            val dbHelper = DBHelper.getInstance(this@MainActivity)
            try {
                val db = dbHelper.readableDatabase
                val cursor = db.query(
                    "Appeals",
                    arrayOf(
                        "id",
                        "phone_number",
                        "district",
                        "request_data",
                        "description",
                        "isAllow"
                    ),
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

                while (cursor.moveToNext()) {
                    val data = AppealInfo(
                        cursor.getInt(indexId),
                        cursor.getString(indexPhone),
                        cursor.getString(indexDistrict),
                        cursor.getString(indexRequest),
                        cursor.getString(indexDescription),
                        cursor.getInt(indexAllow)

                    )

                    list!!.add(data)
                }
                cursor.close()
                dbHelper.close()
                return true


            } catch (e: SQLException) {
                return false
            }


        }

        override fun onPostExecute(result: Boolean?) {
            adapter!!.differ.submitList(list)
            binding.newsAppealsId.rvNewsId.adapter = adapter
            binding.newsAppealsId.rvNewsId.layoutManager = LinearLayoutManager(this@MainActivity)
            itemClickEvent()

        }

    }


}