package com.example.apppealolder.db

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import androidx.annotation.Nullable
import com.example.apppealolder.ResponseData
import java.sql.SQLException

class AppealsUpdateAsyncTask(
    private val context: Context, private val asyncTaskCallback: AsyncTaskCallback<Nothing>
) : AsyncTask<Int, Unit, ResponseData<Nothing>>() {

    private lateinit var contentValue: ContentValues


    override fun onPreExecute() {
        contentValue = ContentValues()
        contentValue.put("isAllow", 1)
    }

    override fun doInBackground(vararg id: Int?): ResponseData<Nothing> {
        val dbHelper = DBHelper.getInstance(context)
        return try {

            val db = dbHelper.writableDatabase
            db.update(
                "Appeals", contentValue, "id= ?", arrayOf(id.sumOf { it!! }.toString())
            )
            db.close()
            ResponseData(isSuccess = true)
        } catch (e: SQLException) {
            ResponseData(isSuccess = false, exception = e)
        }


    }

    override fun onProgressUpdate(vararg values: Unit?) {
        super.onProgressUpdate(*values)
    }


    override fun onPostExecute(result: ResponseData<Nothing>) {
        if (result.isSuccess) {
            asyncTaskCallback.onSuccess()
        } else {
            asyncTaskCallback.onError(result.exception)
        }
    }


}