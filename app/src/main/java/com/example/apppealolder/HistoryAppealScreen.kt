package com.example.apppealolder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.databinding.HistoryAppealScreenBinding

class HistoryAppealScreen : AppCompatActivity() {
    private val binding: HistoryAppealScreenBinding by viewBinding()
    private var adapter: AppealAdapter? = null
    private var dbHelper: DBHelper? = null
    private var arrayList: ArrayList<AppealInfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_appeal_screen)
        manageBotNavView()
        loadData()
        itemClickEvent()

    }

    private fun loadData() {
        adapter = AppealAdapter(LabelWord.label)
        adapter!!.differ.submitList(getData())
        binding.historyId.rvNewsId.adapter = adapter
        binding.historyId.rvNewsId.layoutManager = LinearLayoutManager(this)
    }

    private fun getData(): ArrayList<AppealInfo> {
        dbHelper = DBHelper.getInstance(this)
        val db = dbHelper?.readableDatabase
        arrayList = ArrayList()
        val cursor = db?.query(
            "Appeals",
            arrayOf("id", "phone_number", "district", "request_data", "description", "isAllow"),
            "isAllow=?",
            arrayOf("1"),
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {
            val data = AppealInfo(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getInt(5),
            )
            arrayList!!.add(data)
        }
        cursor.close()
        return arrayList!!
    }

    private fun itemClickEvent() {
        adapter?.onItemClick = {
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


}