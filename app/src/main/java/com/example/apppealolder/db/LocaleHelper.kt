package com.example.apppealolder.db

import android.content.Context
import android.content.res.Configuration
import com.example.apppealolder.model.LabelWord.Companion.ENG
import com.example.apppealolder.model.LabelWord.Companion.SHARED_KEY
import com.example.apppealolder.model.LabelWord.Companion.SHARED_LANG
import java.util.Locale

class LocaleHelper {

    companion object {

        fun changeLanguage(lang: String, context: Context) {
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            saveLang(context, lang)
        }

        private fun saveLang(context: Context, lang: String) {
            val sharedPreferences = context.getSharedPreferences(SHARED_LANG, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(SHARED_KEY, lang).apply()
        }

        fun savedLang(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(SHARED_LANG, Context.MODE_PRIVATE)
            return sharedPreferences.getString(SHARED_KEY, ENG).toString()

        }

    }


}


