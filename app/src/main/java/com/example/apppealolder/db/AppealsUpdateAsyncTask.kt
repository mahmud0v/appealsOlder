package com.example.apppealolder.db

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import com.example.apppealolder.ResponseData
import java.sql.SQLException

class AppealsUpdateAsyncTask(
    private val context: Context, private val asyncTaskCallback: AsyncTaskCallback<Int>
) : AsyncTask<Int, Unit, ResponseData<Int>>() {

    private lateinit var contentValue: ContentValues


    override fun onPreExecute() {
        contentValue = ContentValues()
        contentValue.put("isAllow", 1)
    }

    override fun doInBackground(vararg id: Int?): ResponseData<Int> {
        val dbHelper = DBHelper.getInstance(context)
        return try {

            val db = dbHelper.writableDatabase
            db.update(
                "Appeals", contentValue, "id= ?", arrayOf(id.sumOf { it!! }.toString())
            )
            db.close()
            ResponseData(isSuccess = true,1)
        } catch (e: SQLException) {
            ResponseData(isSuccess = false, exception = e)
        }


    }

    override fun onProgressUpdate(vararg values: Unit?) {
        super.onProgressUpdate(*values)
    }


    override fun onPostExecute(result: ResponseData<Int>) {
        if (result.isSuccess) {
            asyncTaskCallback.onSuccess(result.data)
        } else {
            asyncTaskCallback.onError(result.exception)
        }
    }


}