package com.example.apppealolder.screen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.db.LocaleHelper
import com.example.apppealolder.R
import com.example.apppealolder.databinding.SettingsActivityBinding
import com.example.apppealolder.model.LabelWord.Companion.MODE_LIGHT
import com.example.apppealolder.model.LabelWord.Companion.MODE_NIGHT
import com.example.apppealolder.model.LabelWord.Companion.SHARED_KEY
import com.example.apppealolder.model.LabelWord.Companion.SHARED_MODE
import com.example.apppealolder.utils.unVisible
import com.example.apppealolder.utils.visible

class SettingsActivity : AppCompatActivity() {
    private val binding: SettingsActivityBinding by viewBinding()
    private lateinit var modeSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        binding.botNavId.selectedItemId = R.id.profileScreen
        binding.botNavId.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.newAppealsScreen -> {
                    change(NewAppealActivity())
                    true
                }
                R.id.historyAppealsScreen -> {
                    change(HistoryAppealActivity())
                    true
                }


                else -> true

            }
        }

        binding.langLayoutId.setOnClickListener {
            showDialog()
        }
        switchMode()
        createModeSharedPref()
        checkMode()


    }


    private fun change(activity: AppCompatActivity) {
        startActivity(Intent(this, activity::class.java))
        finish()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, NewAppealActivity::class.java))
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
        startActivity(Intent(this, SettingsActivity::class.java))
        finish()

    }

    private fun checkLang(checkList: ArrayList<View>, lang: String) {
        for (view in checkList) {
            if (view.tag.toString() == lang) {
                view.visible()
            } else {
                view.unVisible()
            }
        }

    }

    private fun switchMode() {
        binding.switchId.setOnClickListener {
            if (binding.switchId.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                modeSharedPref.edit().putString(SHARED_KEY, MODE_NIGHT).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                modeSharedPref.edit().putString(SHARED_KEY, MODE_LIGHT).apply()

            }
        }
    }

    private fun checkMode() {
        val mode = modeSharedPref.getString(SHARED_KEY, MODE_LIGHT)
        binding.switchId.isChecked = mode != MODE_LIGHT
    }

    private fun createModeSharedPref() {
        modeSharedPref = getSharedPreferences(SHARED_MODE, Context.MODE_PRIVATE)
    }


}