package com.example.apppealolder.db

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import java.sql.SQLException

class AppealsAsyncTask(
    private val context: Context,
) : AsyncTask<Int, Unit, Boolean>() {

    private lateinit var contentValue: ContentValues


    override fun onPreExecute() {
        contentValue = ContentValues()
        contentValue.put("isAllow", 1)
    }

    override fun doInBackground(vararg id: Int?): Boolean {
        val dbHelper = DBHelper.getInstance(context)
        return try {

            val db = dbHelper.writableDatabase
            db.update(
                "Appeals", contentValue, "id= ?", arrayOf(id.sumOf { it!! }.toString())
            )
            db.close()
            true
        } catch (e: SQLException) {
            false
        }


    }

    override fun onProgressUpdate(vararg values: Unit?) {
        super.onProgressUpdate(*values)
    }


    override fun onPostExecute(result: Boolean?) {

    }


}