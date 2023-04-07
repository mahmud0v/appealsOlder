package com.example.apppealolder.db

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.example.apppealolder.ResponseData
import java.sql.SQLException

class AppealsAsyncTask(
    private val context: Context, private val asyncTaskCallback: AsyncTaskCallback
) : AsyncTask<Int, Unit, ResponseData>() {

    private val contentValue: ContentValues by lazy { ContentValues() }


    override fun onPreExecute() {
        contentValue.put("isAllow", 1)
    }

    override fun doInBackground(vararg id: Int?): ResponseData? {
        val dbHelper = DBHelper.getInstance(context)
        return try {

            val db = dbHelper.writableDatabase
            db.update(
                "Appeals", contentValue, "id= ?", arrayOf(id.sumOf { it!! }.toString())
            )
            db.close()
            ResponseData(true, "Success")
        } catch (e: SQLException) {
            ResponseData(false, e.message.toString())
        }


    }

    override fun onProgressUpdate(vararg values: Unit?) {
        super.onProgressUpdate(*values)
    }


    override fun onPostExecute(result: ResponseData) {
        if (result.isSuccess) {
            asyncTaskCallback.onSuccess(null)
        } else {
            asyncTaskCallback.onError(result.message)
        }
    }


}