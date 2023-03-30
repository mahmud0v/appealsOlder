package com.example.apppealolder

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import java.sql.SQLException

class AppealsAsyncTask(private val context: Context) : AsyncTask<Int, Unit, Boolean>() {

    private var contentValue: ContentValues? = null

    override fun onPreExecute() {
        contentValue = ContentValues()
        contentValue!!.put("isAllow", 1)
    }

    override fun doInBackground(vararg id: Int?): Boolean {
        return try {
            val dbHelper = DBHelper.getInstance(context)

            val db = dbHelper.writableDatabase
            db.update(
                "Appeals", contentValue!!, "id= ?", arrayOf(id.sumOf { it!! }.toString()))
            db.close()
            true
        } catch (e: SQLException) {
            false
        }


    }


    override fun onPostExecute(result: Boolean?) {
        Toast.makeText(context, "$result", Toast.LENGTH_SHORT).show()
    }


}