package com.example.apppealolder.db

import android.content.Context
import android.content.res.Configuration
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
            val sharedPreferences = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("key", lang).apply()
        }

        fun savedLang(context: Context): String {
            val sharedPreferences = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            return sharedPreferences.getString("key", "en").toString()

        }

    }


}


