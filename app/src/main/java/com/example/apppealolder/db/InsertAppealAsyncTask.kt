package com.example.apppealolder.db

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import com.example.apppealolder.ResponseData
import java.sql.SQLException

class InsertAppealAsyncTask(
    private val context: Context,
    private val asyncTaskCallback: AsyncTaskCallback<Nothing>
) : AsyncTask<ContentValues, Unit, ResponseData<Nothing>>() {

    override fun doInBackground(vararg contentValue: ContentValues?): ResponseData<Nothing> {
        val dbHelper = DBHelper.getInstance(context)
        try {
            val db = dbHelper.writableDatabase
            db.insert("Appeals", null, contentValue[0])
            return ResponseData(isSuccess = true)

        } catch (e: SQLException) {
            return ResponseData(isSuccess = false, exception = e)
        }
    }

    override fun onPostExecute(result: ResponseData<Nothing>) {
        if (result.isSuccess) {
            asyncTaskCallback.onSuccess()
        } else {
            asyncTaskCallback.onError(result.exception)
        }
    }


}