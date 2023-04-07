package com.example.apppealolder.db

import android.content.Context
import android.os.AsyncTask
import com.example.apppealolder.ResponseData
import com.example.apppealolder.model.AppealInfo
import java.sql.SQLException

class MainAsyncTask(
    private val context: Context,
    private val asyncTaskCallback: AsyncTaskCallback
) : AsyncTask<Unit, Unit, ResponseData>() {

    private val list: ArrayList<AppealInfo> by lazy { ArrayList() }

    override fun onPreExecute() {
        asyncTaskCallback.onLoading()

    }

    override fun doInBackground(vararg p0: Unit?): ResponseData {
        val dbHelper = DBHelper.getInstance(context)
        try {
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                "Appeals",
                arrayOf(
                    "id",
                    "phone_number",
                    "district",
                    "request_data",
                    "description",
                    "isAllow"
                ),
                "isAllow= ?",
                arrayOf("0"),
                null,
                null,
                null
            )

            val indexId = cursor.getColumnIndex("id")
            val indexPhone = cursor.getColumnIndex("phone_number")
            val indexDistrict = cursor.getColumnIndex("district")
            val indexRequest = cursor.getColumnIndex("request_data")
            val indexDescription = cursor.getColumnIndex("description")
            val indexAllow = cursor.getColumnIndex("isAllow")

            while (cursor.moveToNext()) {
                val data = AppealInfo(
                    cursor.getInt(indexId),
                    cursor.getString(indexPhone),
                    cursor.getString(indexDistrict),
                    cursor.getString(indexRequest),
                    cursor.getString(indexDescription),
                    cursor.getInt(indexAllow)

                )

                list.add(data)
            }
            cursor.close()
            dbHelper.close()
            return ResponseData(true, "Success")

        } catch (e: SQLException) {
            return ResponseData(false, "${e.message}")
        }


    }

    override fun onPostExecute(result: ResponseData) {
        if (result.isSuccess) {
            asyncTaskCallback.onSuccess(list)
        } else {
            asyncTaskCallback.onError(result.message)
        }
    }

}


