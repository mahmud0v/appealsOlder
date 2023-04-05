package com.example.apppealolder.db

import android.content.Context
import android.os.AsyncTask
import com.example.apppealolder.ResponseData
import com.example.apppealolder.model.AppealInfo
import java.sql.SQLException

class HistoryAsyncTask(
    private val context: Context,
    private val asyncTaskCallback: AsyncTaskCallback
) : AsyncTask<Unit, Unit, ResponseData>() {
    private var list: ArrayList<AppealInfo>? = null

    override fun onPreExecute() {
        list = ArrayList()
        asyncTaskCallback.onLoading()
    }


    override fun doInBackground(vararg p0: Unit?): ResponseData? {
        val dbHelper = DBHelper.getInstance(context)
        try {
            val db = dbHelper?.readableDatabase
            val cursor = db?.query(
                "Appeals",
                arrayOf(
                    "id",
                    "phone_number",
                    "district",
                    "request_data",
                    "description",
                    "isAllow"
                ),
                "isAllow=?",
                arrayOf("1"),
                null,
                null,
                null
            )
            while (cursor!!.moveToNext()) {
                val data = AppealInfo(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                )
                list!!.add(data)
            }
            cursor.close()
            dbHelper!!.close()
            return ResponseData(true, "Success")
//            throw SQLException("Sadasdasdasdasdasd")

        } catch (e: SQLException) {
            return ResponseData(false, "${e.message}")
        }

    }

    override fun onPostExecute(result: ResponseData?) {
        if (result!!.isSuccess) {
            asyncTaskCallback.onSuccess(list!!)
        } else {
            asyncTaskCallback.onError(result.message)
        }
    }


}
