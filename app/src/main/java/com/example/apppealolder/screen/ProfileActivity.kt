package com.example.apppealolder.screen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.db.LocaleHelper
import com.example.apppealolder.R
import com.example.apppealolder.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private val binding: ActivityProfileBinding by viewBinding()
    private lateinit var modeSharedPref: SharedPreferences

    //    private val dialog:Dialog by lazy { Dialog(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        manageBotNavView()
        modeBtnClick()
        switchMode()
        createModeSharedPref()
        checkMode()


    }


    private fun manageBotNavView() {
        binding.botNavId.selectedItemId = R.id.profileScreen
        binding.botNavId.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.newAppealsScreen -> {
                    change(MainActivity())
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun modeBtnClick() {
        binding.langId.langLayoutId.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_screen)
        val english: LinearLayout = dialog.findViewById(R.id.english)
        val russian: LinearLayout = dialog.findViewById(R.id.russian)
        val uzbek: LinearLayout = dialog.findViewById(R.id.uzbek)
        val layoutList = arrayListOf(english, russian, uzbek)
        val checkList =
            arrayListOf(english.getChildAt(1), russian.getChildAt(1), uzbek.getChildAt(1))
        val lang = LocaleHelper.savedLang(this)
        checkLang(checkList, lang)
        for (i in layoutList) {
            i.setOnClickListener {
                checkLang(checkList, i.tag.toString())
                dialog.dismiss()
                changeLang(i.tag.toString())
            }
        }

        dialog.show()

    }


    private fun changeLang(lang: String) {
        LocaleHelper.changeLanguage(lang, this)
        startActivity(Intent(this, ProfileActivity::class.java))
        finish()

    }

    private fun checkLang(checkList: ArrayList<View>, lang: String) {
        for (i in checkList) {
            if (i.tag.toString() == lang) {
                i.visibility = View.VISIBLE
            } else {
                i.visibility = View.GONE
            }
        }

    }

    private fun switchMode() {
        binding.langId.switchId.setOnClickListener {
            if (binding.langId.switchId.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                modeSharedPref.edit().putString("key", "night").apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                modeSharedPref.edit().putString("key", "light").apply()

            }
        }
    }

    private fun checkMode() {
        val mode = modeSharedPref.getString("key", "light")
        binding.langId.switchId.isChecked = mode != "light"
    }

    private fun createModeSharedPref() {
        modeSharedPref = getSharedPreferences("modeShared", Context.MODE_PRIVATE)
    }


}