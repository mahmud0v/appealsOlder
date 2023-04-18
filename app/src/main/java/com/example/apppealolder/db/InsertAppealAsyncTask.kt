package com.example.apppealolder.db

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import com.example.apppealolder.ResponseData
import com.example.apppealolder.model.AppealInfo
import java.sql.SQLException

class InsertAppealAsyncTask(
    private val context: Context,
    private val asyncTaskCallback: AsyncTaskCallback<AppealInfo>
) : AsyncTask<ContentValues, Unit, ResponseData<AppealInfo>>() {

    override fun doInBackground(vararg contentValue: ContentValues?): ResponseData<AppealInfo> {
        val dbHelper = DBHelper.getInstance(context)
        try {
            val db = dbHelper.writableDatabase
            db.insert("Appeals", null, contentValue[0])
            var lastData: AppealInfo? = null
            var cursor = db.query(
                "Appeals",
                arrayOf("id", "phone_number", "district", "request_data", "description", "isAllow"),
                null,
                null,
                null,
                null,
                null
            )
            if (cursor.moveToLast()) {
                lastData = AppealInfo(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5)
                )
            }

            return ResponseData(isSuccess = true, data = lastData)

        } catch (e: SQLException) {
            return ResponseData(isSuccess = false, exception = e)
        }
    }

    override fun onPostExecute(result: ResponseData<AppealInfo>) {
        if (result.isSuccess) {
            asyncTaskCallback.onSuccess(result.data)
        } else {
            asyncTaskCallback.onError(result.exception)
        }
    }


}